package co.com.bancolombia.r2dbc.repository;

import co.com.bancolombia.model.response.LoanPetitionResponse;
import co.com.bancolombia.r2dbc.entity.LoanPetitionEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LoanPetitionReactiveRepository extends ReactiveCrudRepository<LoanPetitionEntity, Long>, ReactiveQueryByExampleExecutor<LoanPetitionEntity> {
    Flux<LoanPetitionEntity> findAllByEmail(String email);

    Flux<LoanPetitionEntity> findAllByDocumentNumber(String documentNumber);

    @Query("""
            WITH base AS (
                SELECT lp.id AS id,
                       lp.amount AS amount,
                       lp.term AS term,
                       lp.email AS email,
                       lp.document_number as document_number,
                       lt.interest_rate AS interest_rate,
                       lt.name AS loan_petition_type,
                       s.name AS loan_petition_state,
                       lp.state_id AS state_id,
                       lp.loan_type_id AS loan_type_id
                FROM loan_petitions lp
                JOIN loan_types lt ON lt.id = lp.loan_type_id
                JOIN states s ON s.id = lp.state_id
            ),
            agg AS (
                SELECT document_number,
                       COALESCE(SUM(
                           CASE WHEN (lt.interest_rate/100) = 0 THEN lp.amount / NULLIF(lp.term, 0)
                                ELSE lp.amount * (lt.interest_rate/100) / (1 - POWER(1 + (lt.interest_rate/100), -lp.term))
                           END
                       ), 0) AS total_monthly_approved
                FROM loan_petitions lp
                JOIN loan_types lt ON lt.id = lp.loan_type_id
                JOIN states s ON s.id = lp.state_id
                WHERE UPPER(s.name) LIKE 'APROB%'
                GROUP BY lp.document_number
            )
            SELECT 
                b.id AS id,
                b.amount AS amount,
                b.term AS term,
                b.email AS email,
                b.document_number ,
                b.loan_petition_type ,
                b.interest_rate ,
                b.loan_petition_state ,
                COALESCE(a.total_monthly_approved, 0) AS total_monthly_approved
            FROM base b
            LEFT JOIN agg a ON a.document_number = b.document_number
            WHERE (:stateId IS NULL OR b.state_id = :stateId)
              AND (:loanTypeId IS NULL OR b.loan_type_id = :loanTypeId)
              AND (:doc IS NULL OR b.document_number = :doc)
            ORDER BY b.id DESC
            LIMIT :size OFFSET :offset
            """)
    Flux<LoanPetitionResponse> findLoanPetitionsPageFiltered(
            @Param("stateId") Integer stateId,
            @Param("loanTypeId") Long loanTypeId,
            @Param("doc") String doc,
            @Param("size") int size,
            @Param("offset") int offset);

    @Query("""
            SELECT COUNT(*) AS total
            FROM loan_petitions lp
            WHERE (:stateId    IS NULL OR lp.state_id     = :stateId)
              AND (:loanTypeId IS NULL OR lp.loan_type_id = :loanTypeId)
              AND (:doc        IS NULL OR lp.document_number = :doc)
            """)
    Mono<Long> countFiltered(
            @Param("stateId") Integer stateId,
            @Param("loanTypeId") Long loanTypeId,
            @Param("doc") String doc
    );
}
