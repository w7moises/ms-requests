package co.com.bancolombia.r2dbc.repository;

import co.com.bancolombia.r2dbc.entity.LoanPetitionEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface LoanPetitionReactiveRepository extends ReactiveCrudRepository<LoanPetitionEntity, Long>, ReactiveQueryByExampleExecutor<LoanPetitionEntity> {
    Flux<LoanPetitionEntity> findAllByEmail(String email);

    Flux<LoanPetitionEntity> findAllByDocumentNumber(String documentNumber);
}
