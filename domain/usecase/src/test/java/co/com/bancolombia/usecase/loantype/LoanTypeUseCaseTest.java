package co.com.bancolombia.usecase.loantype;

import co.com.bancolombia.model.loantype.LoanType;
import co.com.bancolombia.model.loantype.gateways.LoanTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoanTypeUseCaseTest {

    @Mock
    LoanTypeRepository repository;

    LoanTypeUseCase useCase;

    private static final LoanType LOAN_MICRO = LoanType.builder()
            .id(1L)
            .name("MICRO")
            .minAmount(new BigDecimal("100.00"))
            .maxAmount(new BigDecimal("1000.00"))
            .interestRate(new BigDecimal("0.12"))
            .automaticValidation(Boolean.TRUE)
            .build();

    private static final LoanType LOAN_PERSONAL = LoanType.builder()
            .id(2L)
            .name("PERSONAL")
            .minAmount(new BigDecimal("500.00"))
            .maxAmount(new BigDecimal("5000.00"))
            .interestRate(new BigDecimal("0.18"))
            .automaticValidation(Boolean.FALSE)
            .build();

    private static final LoanType LOAN_NEW = LoanType.builder()
            .name("EMPRESARIAL")
            .minAmount(new BigDecimal("1000.00"))
            .maxAmount(new BigDecimal("10000.00"))
            .interestRate(new BigDecimal("0.20"))
            .automaticValidation(Boolean.FALSE)
            .build();

    @BeforeEach
    void setUp() {
        useCase = new LoanTypeUseCase(repository);
    }

    @Test
    void shouldSaveLoanType() {
        var saved = LOAN_NEW.toBuilder().id(10L).build();
        when(repository.saveLoanType(LOAN_NEW)).thenReturn(Mono.just(saved));
        StepVerifier.create(useCase.saveLoanType(LOAN_NEW))
                .expectNext(saved)
                .verifyComplete();
    }

    @Test
    void shouldUpdateLoanType() {
        LoanType updated = LOAN_MICRO.toBuilder().interestRate(new BigDecimal("0.15")).build();
        when(repository.updateLoanType(updated)).thenReturn(Mono.just(updated));
        StepVerifier.create(useCase.updateLoanType(updated))
                .expectNext(updated)
                .verifyComplete();
    }

    @Test
    void shouldDeleteLoanType() {
        when(repository.deleteLoanType(2L)).thenReturn(Mono.empty());
        StepVerifier.create(useCase.deleteLoanType(2L))
                .verifyComplete();
    }

    @Test
    void shouldFindLoanTypeById() {
        when(repository.findLoanTypeById(1L)).thenReturn(Mono.just(LOAN_MICRO));
        StepVerifier.create(useCase.findLoanTypeById(1L))
                .expectNext(LOAN_MICRO)
                .verifyComplete();
    }

    @Test
    void shouldFindAllLoanTypes() {
        when(repository.findAllLoanTypes()).thenReturn(Flux.just(LOAN_MICRO, LOAN_PERSONAL));
        StepVerifier.create(useCase.findAllLoanTypes())
                .expectNext(LOAN_MICRO)
                .expectNext(LOAN_PERSONAL)
                .verifyComplete();
    }
}
