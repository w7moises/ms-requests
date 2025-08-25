package co.com.bancolombia.model.loantype.gateways;

import co.com.bancolombia.model.loantype.LoanType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface LoanTypeRepository {
    Mono<LoanType> saveLoanType(LoanType loanType);

    Mono<LoanType> updateLoanType(LoanType loanType);

    Mono<Void> deleteLoanType(Long id);

    Mono<LoanType> findLoanTypeById(Long id);

    Mono<LoanType> findLoanTypeByMinAndMaxAmount(BigDecimal amount);

    Flux<LoanType> findAllLoanTypes();
}
