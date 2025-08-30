package co.com.bancolombia.r2dbc.repository;

import co.com.bancolombia.model.loanpetition.LoanPetition;
import co.com.bancolombia.model.loanpetition.gateways.LoanPetitionRepository;
import co.com.bancolombia.model.response.LoanPetitionResponse;
import co.com.bancolombia.r2dbc.entity.LoanPetitionEntity;
import co.com.bancolombia.r2dbc.exception.NotFoundException;
import co.com.bancolombia.r2dbc.helper.ReactiveAdapterOperations;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
public class LoanPetitionReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        LoanPetition,
        LoanPetitionEntity,
        Long,
        LoanPetitionReactiveRepository
        > implements LoanPetitionRepository {

    public LoanPetitionReactiveRepositoryAdapter(LoanPetitionReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, LoanPetition.class));
    }

    @Transactional(transactionManager = "r2dbcTransactionManager")
    @Override
    public Mono<LoanPetition> savePetition(LoanPetition loanPetition) {
        log.info("Saving loan Petition {}", loanPetition);
        return super.save(loanPetition);
    }

    @Transactional(transactionManager = "r2dbcTransactionManager", readOnly = true)
    @Override
    public Flux<LoanPetition> findAllPetitions() {
        return super.findAll();
    }

    @Transactional(transactionManager = "r2dbcTransactionManager", readOnly = true)
    @Override
    public Flux<LoanPetition> findAllPetitionsByEmail(String email) {
        return repository.findAllByEmail(email)
                .map(this::toEntity)
                .switchIfEmpty(Mono.error(new NotFoundException("loanPetition.notFound.email", email)));
    }

    @Transactional(transactionManager = "r2dbcTransactionManager", readOnly = true)
    @Override
    public Flux<LoanPetition> findAllPetitionsByDocumentNumber(String documentNumber) {
        return repository.findAllByDocumentNumber(documentNumber)
                .map(this::toEntity)
                .switchIfEmpty(Mono.error(new NotFoundException("loanPetition.notFound.documentNumber", documentNumber)));
    }

    @Transactional(transactionManager = "r2dbcTransactionManager", readOnly = true)
    @Override
    public Flux<LoanPetitionResponse> findLoanPetitionsPageFiltered(Integer stateId, Long loanTypeId, String doc, int size, int offset) {
        return repository.findLoanPetitionsPageFiltered(stateId, loanTypeId, doc, size, offset);
    }

    @Transactional(transactionManager = "r2dbcTransactionManager", readOnly = true)
    @Override
    public Mono<Long> countFiltered(Integer stateId, Long loanTypeId, String doc) {
        return repository.countFiltered(stateId, loanTypeId, doc);
    }
}
