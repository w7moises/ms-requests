package co.com.bancolombia.r2dbc.repository;

import co.com.bancolombia.r2dbc.entity.LoanTypeEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface LoanTypeReactiveRepository extends ReactiveCrudRepository<LoanTypeEntity, Long>, ReactiveQueryByExampleExecutor<LoanTypeEntity> {
    @Query("select * from loan_types where min_amount <= :amount and max_amount >= :amount")
    Mono<LoanTypeEntity> findLoanTypeByAmount(BigDecimal amount);
}
