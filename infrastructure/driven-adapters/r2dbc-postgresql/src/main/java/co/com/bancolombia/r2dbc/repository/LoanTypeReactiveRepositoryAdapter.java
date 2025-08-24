package co.com.bancolombia.r2dbc.repository;

import co.com.bancolombia.model.loantype.LoanType;
import co.com.bancolombia.model.loantype.gateways.LoanTypeRepository;
import co.com.bancolombia.r2dbc.entity.LoanTypeEntity;
import co.com.bancolombia.r2dbc.exception.NotFoundException;
import co.com.bancolombia.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class LoanTypeReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        LoanType,
        LoanTypeEntity,
        Long,
        LoanTypeReactiveRepository
        > implements LoanTypeRepository {
    public LoanTypeReactiveRepositoryAdapter(LoanTypeReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, LoanType.class));
    }

    @Transactional(transactionManager = "r2dbcTransactionManager")
    @Override
    public Mono<LoanType> saveLoanType(LoanType loanType) {
        return super.save(loanType);
    }

    @Transactional(transactionManager = "r2dbcTransactionManager")
    @Override
    public Mono<LoanType> updateLoanType(LoanType loanType) {
        return super.findById(loanType.getId())
                .switchIfEmpty(Mono.error(new NotFoundException("loanType.notFound.id", loanType.getId())))
                .flatMap(data -> super.save(loanType));
    }

    @Transactional(transactionManager = "r2dbcTransactionManager")
    @Override
    public Mono<Void> deleteLoanType(Long id) {
        return super.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("loanType.notFound.id", id)))
                .flatMap(entity -> repository.deleteById(entity.getId()));
    }

    @Transactional(transactionManager = "r2dbcTransactionManager", readOnly = true)
    @Override
    public Mono<LoanType> findLoanTypeById(Long id) {
        return super.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("loanType.notFound.id", id)));
    }

    @Transactional(transactionManager = "r2dbcTransactionManager", readOnly = true)
    @Override
    public Flux<LoanType> findAllLoanTypes() {
        return super.findAll();
    }
}
