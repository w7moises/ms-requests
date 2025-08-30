package co.com.bancolombia.usecase.loanpetition;

import co.com.bancolombia.model.loanpetition.LoanPetition;
import co.com.bancolombia.model.loanpetition.gateways.LoanPetitionRepository;
import co.com.bancolombia.model.loantype.LoanType;
import co.com.bancolombia.model.loantype.gateways.LoanTypeRepository;
import co.com.bancolombia.model.response.*;
import co.com.bancolombia.model.state.State;
import co.com.bancolombia.model.state.gateways.StateRepository;
import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class LoanPetitionUseCase {
    private final LoanPetitionRepository loanPetitionRepository;
    private final UserRepository userRepository;
    private final StateRepository stateRepository;
    private final LoanTypeRepository loanTypeRepository;

    public Mono<LoanPetition> savePetition(LoanPetition loanPetition) {
        Mono<LoanType> loanTypeMono = loanTypeRepository.findLoanTypeByMinAndMaxAmount(loanPetition.getAmount());
        Mono<User> userMono = userRepository.findUserByDocumentNumber(loanPetition.getDocumentNumber());
        Mono<State> stateMono = stateRepository.findAllStates()
                .filter(state -> "PENDIENTE DE REVISION".equalsIgnoreCase(state.getName()))
                .next();
        return Mono.zip(loanTypeMono, userMono, stateMono)
                .flatMap(tuple -> {
                    LoanType loanType = tuple.getT1();
                    User user = tuple.getT2();
                    State state = tuple.getT3();
                    loanPetition.setEmail(user.getEmail());
                    loanPetition.setLoanTypeId(loanType.getId());
                    loanPetition.setStateId(state.getId());
                    return loanPetitionRepository.savePetition(loanPetition);
                });
    }

    public Flux<LoanPetition> findAllPetitions() {
        return loanPetitionRepository.findAllPetitions();
    }

    public Mono<PagedDataResponse> findAllPetitionsFiltered(
            Integer stateId, Long loanTypeId, String document,
            int page, int size) {

        int safeSize = Math.max(1, size);
        int offset = Math.max(0, page) * safeSize;
        Mono<List<LoanPetitionResponse>> rowsMono = loanPetitionRepository
                .findLoanPetitionsPageFiltered(stateId, loanTypeId, document, safeSize, offset)
                .collectList();
        Mono<Long> totalMono = loanPetitionRepository.countFiltered(stateId, loanTypeId, document);

        return Mono.zip(rowsMono, totalMono).flatMap(tuple -> {
            List<LoanPetitionResponse> rows = tuple.getT1();

            long total = tuple.getT2();

            LinkedHashSet<String> docs = rows.stream()
                    .map(LoanPetitionResponse::getDocumentNumber)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toCollection(LinkedHashSet::new));

            Mono<Map<String, User>> users = Flux.fromIterable(docs)
                    .flatMap(dn -> userRepository.findUserByDocumentNumber(dn)
                            .onErrorResume(e -> Mono.just(User.builder().documentNumber(dn).build())), 6)
                    .collectMap(User::getDocumentNumber, u -> u);

            Map<String, List<PetitionItemDto>> itemsByDoc = new LinkedHashMap<>();
            Map<String, BigDecimal> totalMonthlyByDoc = new LinkedHashMap<>();

            for (LoanPetitionResponse r : rows) {
                String documentNumber = r.getDocumentNumber();
                PetitionItemDto item = PetitionItemDto.builder()
                        .id(r.getId())
                        .amount(r.getAmount())
                        .term(r.getTerm())
                        .email(r.getEmail())
                        .documentNumber(documentNumber)
                        .loanPetitionType(r.getLoanPetitionType())
                        .interestRate(r.getInterestRate())
                        .loanPetitionState(r.getLoanPetitionState())
                        .build();
                itemsByDoc.computeIfAbsent(documentNumber, k -> new ArrayList<>()).add(item);
                totalMonthlyByDoc.putIfAbsent(documentNumber,
                        Optional.ofNullable(r.getTotalMonthlyApproved()).orElse(BigDecimal.ZERO));
            }
            return users.map(usersByDoc -> {
                List<DataGroupDto> data = new ArrayList<>(itemsByDoc.size());
                for (Map.Entry<String, List<PetitionItemDto>> e : itemsByDoc.entrySet()) {
                    String documentNumber = e.getKey();
                    User u = usersByDoc.getOrDefault(documentNumber, User.builder().documentNumber(documentNumber).build());
                    UserDto userDto = UserDto.builder()
                            .documentNumber(u.getDocumentNumber())
                            .name(u.getName())
                            .lastName(u.getLastName())
                            .email(u.getEmail())
                            .salary(u.getSalary())
                            .build();
                    data.add(DataGroupDto.builder()
                            .user(userDto)
                            .loanPetitions(e.getValue())
                            .totalMonthlyDebt(totalMonthlyByDoc.getOrDefault(documentNumber, BigDecimal.ZERO))
                            .build());
                }
                int totalPages = (int) Math.ceil((double) total / safeSize);
                return PagedDataResponse.builder()
                        .page(PageDto.builder()
                                .number(page)
                                .size(safeSize)
                                .totalElements(total)
                                .totalPages(totalPages)
                                .build())
                        .data(data)
                        .build();
            });
        });
    }

    public Flux<LoanPetition> findAllPetitionsByEmail(String email) {
        return loanPetitionRepository.findAllPetitionsByEmail(email);
    }

    public Flux<LoanPetition> findAllPetitionsByDocumentNumber(String documentNumber) {
        return loanPetitionRepository.findAllPetitionsByDocumentNumber(documentNumber);
    }
}
