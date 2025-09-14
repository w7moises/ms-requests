package co.com.bancolombia.model.loanpetition.gateways;

import co.com.bancolombia.model.loanpetition.LoanPetition;
import co.com.bancolombia.model.response.LoanPetitionInformation;
import co.com.bancolombia.model.response.LoanPetitionResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LoanPetitionRepository {
    Mono<LoanPetition> savePetition(LoanPetition loanPetition);

    Mono<LoanPetition> findPetitionById(Long id);

    Mono<LoanPetition> changePetitionStatus(Long status, Long petitionId);

    Flux<LoanPetition> findAllPetitions();

    Flux<LoanPetition> findAllPetitionsByEmail(String email);

    Flux<LoanPetition> findAllPetitionsByDocumentNumber(String documentNumber);

    Flux<LoanPetitionInformation> findAllPetitionInformationByDocumentNumber(String documentNumber, Long id);

    Flux<LoanPetitionResponse> findLoanPetitionsPageFiltered(Integer stateId, Long loanTypeId, String doc,
                                                             int size, int offset);

    Mono<Long> countFiltered(Integer stateId, Long loanTypeId, String doc);

}