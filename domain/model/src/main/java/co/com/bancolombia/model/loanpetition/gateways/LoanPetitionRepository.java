package co.com.bancolombia.model.loanpetition.gateways;

import co.com.bancolombia.model.loanpetition.LoanPetition;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LoanPetitionRepository {
    Mono<LoanPetition> savePetition(LoanPetition loanPetition);

    Flux<LoanPetition> findAllPetitions();

    Flux<LoanPetition> findAllPetitionsByEmail(String email);

    Flux<LoanPetition> findAllPetitionsByDocumentNumber(String documentNumber);
}
