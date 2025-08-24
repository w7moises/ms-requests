package co.com.bancolombia.usecase.loantype;

import co.com.bancolombia.model.loantype.LoanType;
import co.com.bancolombia.model.loantype.gateways.LoanTypeRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class LoanTypeUseCase {
    private final LoanTypeRepository loanTypeRepository;

    public Mono<LoanType> saveLoanType(LoanType loanType) {
        return loanTypeRepository.saveLoanType(loanType);
    }

    public Mono<LoanType> updateLoanType(LoanType loanType) {
        return loanTypeRepository.updateLoanType(loanType);
    }

    public Mono<Void> deleteLoanType(Long id) {
        return loanTypeRepository.deleteLoanType(id);
    }

    public Mono<LoanType> findLoanTypeById(Long id) {
        return loanTypeRepository.findLoanTypeById(id);
    }

    public Flux<LoanType> findAllLoanTypes() {
        return loanTypeRepository.findAllLoanTypes();
    }
}
