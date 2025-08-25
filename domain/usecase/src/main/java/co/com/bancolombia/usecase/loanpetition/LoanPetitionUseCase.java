package co.com.bancolombia.usecase.loanpetition;

import co.com.bancolombia.model.loanpetition.LoanPetition;
import co.com.bancolombia.model.loanpetition.gateways.LoanPetitionRepository;
import co.com.bancolombia.model.loantype.LoanType;
import co.com.bancolombia.model.loantype.gateways.LoanTypeRepository;
import co.com.bancolombia.model.state.State;
import co.com.bancolombia.model.state.gateways.StateRepository;
import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class LoanPetitionUseCase {
    private final LoanPetitionRepository loanPetitionRepository;
    private final UserRepository userRepository;
    private final StateRepository stateRepository;
    private final LoanTypeRepository loanTypeRepository;

    public Mono<LoanPetition> savePetition(LoanPetition loanPetition) {
        return loanTypeRepository.findLoanTypeByMinAndMaxAmount(loanPetition.getAmount())
                .zipWith(userRepository.findUserByDocumentNumber(loanPetition.getDocumentNumber()))
                .zipWith(stateRepository.findAllStates()
                        .filter(state -> "APROBADO".equalsIgnoreCase(state.getName()))
                        .next())
                .flatMap(tuple -> {
                    LoanType loanType = tuple.getT1().getT1();
                    User user = tuple.getT1().getT2();
                    State state = tuple.getT2();
                    loanPetition.setEmail(user.getEmail());
                    loanPetition.setLoanTypeId(loanType.getId());
                    loanPetition.setStateId(state.getId());
                    return loanPetitionRepository.savePetition(loanPetition);
                });
    }

    public Flux<LoanPetition> findAllPetitions() {
        return loanPetitionRepository.findAllPetitions();
    }

    public Flux<LoanPetition> findAllPetitionsByEmail(String email) {
        return loanPetitionRepository.findAllPetitionsByEmail(email);
    }

    public Flux<LoanPetition> findAllPetitionsByDocumentNumber(String documentNumber) {
        return loanPetitionRepository.findAllPetitionsByDocumentNumber(documentNumber);
    }
}
