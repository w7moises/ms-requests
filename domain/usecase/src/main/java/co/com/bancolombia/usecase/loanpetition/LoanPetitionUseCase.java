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
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static co.com.bancolombia.usecase.util.MathUtil.monthlyDebtCalculation;

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

    public Mono<PagedGroupedResponse> findAllPetitionsFiltered(
            Integer stateId, Long loanTypeId, String document,
            int page, int size) {

        Flux<LoanPetition> loanPetitionList = (document != null && !document.isBlank())
                ? loanPetitionRepository.findAllPetitionsByDocumentNumber(document)
                : loanPetitionRepository.findAllPetitions();

        Flux<LoanPetition> loanPetitionListFiltered = loanPetitionList
                .filter(lp -> (stateId == null || (lp.getStateId() != null && lp.getStateId().intValue() == stateId))
                        && (loanTypeId == null || lp.getLoanTypeId() != null && lp.getLoanTypeId().equals(loanTypeId)))
                .cache();
        Mono<List<LoanPetition>> loanPetitionListSuscribe = loanPetitionListFiltered.collectList();
        // 2) Total y paginación determinista (id DESC)
        Mono<Long> totalElementFromList = loanPetitionListSuscribe.map(list -> (long) list.size());

        Mono<List<LoanPetition>> pagedPetitionsMono = loanPetitionListSuscribe.map(list -> {
            list.sort(Comparator.comparing(LoanPetition::getId, Comparator.nullsLast(Long::compareTo)).reversed());
            int fromIdx = Math.max(0, page * size);
            int toIdx = Math.min(list.size(), fromIdx + size);
            if (fromIdx >= toIdx) return List.of();
            return list.subList(fromIdx, toIdx);
        });

        // 3) Catálogos (tipos y estados)
        Mono<Map<Long, LoanType>> loanTypesByIdMono = loanTypeRepository.findAllLoanTypes()
                .collectMap(LoanType::getId, lt -> lt);

        Mono<Map<Long, State>> statesByIdMono = stateRepository.findAllStates()
                .collectMap(State::getId, st -> st);

        return Mono.zip(pagedPetitionsMono, totalElementFromList, loanTypesByIdMono, statesByIdMono)
                .flatMap(tuple -> {
                    List<LoanPetition> pagedPetitions = tuple.getT1();
                    long totalElements = tuple.getT2();
                    Map<Long, LoanType> loanTypesById = tuple.getT3();
                    Map<Long, State> statesById = tuple.getT4();

                    // 4) Documentos únicos de la página
                    LinkedHashSet<String> documentNumbers = pagedPetitions.stream()
                            .map(LoanPetition::getDocumentNumber)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toCollection(LinkedHashSet::new));

                    // 5) Usuarios (async, con fallback mínimo)
                    Mono<Map<String, User>> usersByDocumentMono = Flux.fromIterable(documentNumbers)
                            .flatMap(dn -> userRepository.findUserByDocumentNumber(dn)
                                    .onErrorResume(e -> Mono.just(User.builder().documentNumber(dn).build())), 6)
                            .collectMap(User::getDocumentNumber, u -> u);

                    // 6) Agregado por usuario (sobre la página), usando tasa mensual directamente
                    Map<String, BigDecimal> totalMonthlyDebtPerUser = new ConcurrentHashMap<>();
                    for (LoanPetition lp : pagedPetitions) {
                        String documentNumber = lp.getDocumentNumber();
                        if (documentNumber == null) continue;

                        State st = statesById.get(lp.getStateId());
                        if (st == null) continue;

                        // Solo solicitudes APROBADAS
                        if (!"APROBADO".equalsIgnoreCase(st.getName())) continue;

                        LoanType lt = loanTypesById.get(lp.getLoanTypeId());
                        if (lt == null || lt.getInterestRate() == null || lp.getAmount() == null || lp.getTerm() == null)
                            continue;

                        BigDecimal monthly = monthlyDebtCalculation(lp.getAmount(), lt.getInterestRate(), lp.getTerm());
                        totalMonthlyDebtPerUser.merge(documentNumber, monthly, BigDecimal::add);
                    }

                    // 7) Mapear a DTOs de ítems
                    List<PetitionItemDto> itemDtos = pagedPetitions.stream().map(lp -> {
                        LoanType lt = loanTypesById.get(lp.getLoanTypeId());
                        State st = statesById.get(lp.getStateId());
                        return PetitionItemDto.builder()
                                .id(lp.getId())
                                .amount(lp.getAmount())
                                .term(lp.getTerm())
                                .email(lp.getEmail())
                                .documentNumber(lp.getDocumentNumber())
                                .loanPetitionType(lt != null ? lt.getName() : null)
                                .interestRate(lt != null ? lt.getInterestRate() : null) // mensual
                                .loanPetitionState(st != null ? st.getName() : null)
                                .build();
                    }).toList();

                    Map<String, List<PetitionItemDto>> itemsGroupedByDocument = itemDtos.stream()
                            .collect(Collectors.groupingBy(PetitionItemDto::getDocumentNumber, LinkedHashMap::new, Collectors.toList()));

                    return usersByDocumentMono.map(usersByDocument -> {
                        List<UserGroupDto> groups = new ArrayList<>();
                        for (Map.Entry<String, List<PetitionItemDto>> entry : itemsGroupedByDocument.entrySet()) {
                            String dn = entry.getKey();
                            List<PetitionItemDto> items = entry.getValue();
                            User user = usersByDocument.getOrDefault(dn, User.builder().documentNumber(dn).build());
                            UserDto userDto = UserDto.builder()
                                    .documentNumber(user.getDocumentNumber())
                                    .name(user.getName())
                                    .lastName(user.getLastName())
                                    .email(user.getEmail())
                                    .salary(user.getSalary())
                                    .build();
                            AggregatesDto aggregatesDto = AggregatesDto.builder()
                                    .totalMonthlyDebtAmountApproved(
                                            totalMonthlyDebtPerUser.getOrDefault(dn, BigDecimal.ZERO)
                                    )
                                    .build();
                            groups.add(UserGroupDto.builder()
                                    .user(userDto)
                                    .aggregates(aggregatesDto)
                                    .items(items)
                                    .build());
                        }
                        int totalPages = (int) Math.ceil((double) totalElements / Math.max(1, size));
                        return PagedGroupedResponse.builder()
                                .page(PageDto.builder()
                                        .number(page)
                                        .size(size)
                                        .totalElements(totalElements)
                                        .totalPages(totalPages)
                                        .build())
                                .groups(groups)
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
