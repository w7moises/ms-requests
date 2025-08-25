package co.com.bancolombia.r2dbc;

import co.com.bancolombia.model.loantype.LoanType;
import co.com.bancolombia.r2dbc.entity.LoanTypeEntity;
import co.com.bancolombia.r2dbc.exception.NotFoundException;
import co.com.bancolombia.r2dbc.repository.LoanTypeReactiveRepository;
import co.com.bancolombia.r2dbc.repository.LoanTypeReactiveRepositoryAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanTypeReactiveRepositoryAdapterTest {

    @InjectMocks
    private LoanTypeReactiveRepositoryAdapter adapter;

    @Mock
    private LoanTypeReactiveRepository repository;

    @Mock
    private ObjectMapper mapper;

    private LoanTypeEntity loanTypeEntity;
    private LoanType loanType;

    @BeforeEach
    void setUp() {
        loanTypeEntity = LoanTypeEntity.builder()
                .id(1L)
                .name("Personal")
                .minAmount(BigDecimal.valueOf(1000))
                .maxAmount(BigDecimal.valueOf(7000))
                .interestRate(BigDecimal.valueOf(1))
                .automaticValidation(true)
                .build();
        loanType = LoanType.builder()
                .id(1L)
                .name("Personal")
                .minAmount(BigDecimal.valueOf(1000))
                .maxAmount(BigDecimal.valueOf(7000))
                .interestRate(BigDecimal.valueOf(1))
                .automaticValidation(true)
                .build();
    }

    @Test
    void shouldSaveLoanType() {
        when(mapper.map(loanType, LoanTypeEntity.class)).thenReturn(loanTypeEntity);
        when(repository.save(loanTypeEntity)).thenReturn(Mono.just(loanTypeEntity));
        when(mapper.map(loanTypeEntity, LoanType.class)).thenReturn(loanType);
        StepVerifier.create(adapter.saveLoanType(loanType))
                .expectNext(loanType)
                .verifyComplete();
    }

    @Test
    void shouldNotUpdateLoanTypeIfIdIsNotFound() {
        when(repository.findById(1L)).thenReturn(Mono.empty());
        StepVerifier.create(adapter.updateLoanType(loanType))
                .expectError(NotFoundException.class)
                .verify();
    }

    @Test
    void shouldUpdateLoanType() {
        when(repository.findById(1L)).thenReturn(Mono.just(loanTypeEntity));
        when(mapper.map(loanTypeEntity, LoanType.class)).thenReturn(loanType);
        when(repository.save(loanTypeEntity)).thenReturn(Mono.just(loanTypeEntity));
        when(mapper.map(loanType, LoanTypeEntity.class)).thenReturn(loanTypeEntity);
        StepVerifier.create(adapter.updateLoanType(loanType))
                .expectNext(loanType)
                .verifyComplete();
    }

    @Test
    void shouldFindLoanTypeById() {
        when(repository.findById(1L)).thenReturn(Mono.just(loanTypeEntity));
        when(mapper.map(loanTypeEntity, LoanType.class)).thenReturn(loanType);
        StepVerifier.create(adapter.findLoanTypeById(1L))
                .expectNext(loanType)
                .verifyComplete();
    }

    @Test
    void shouldFindAllLoanTypes() {
        when(mapper.map(loanTypeEntity, LoanType.class)).thenReturn(loanType, loanType);
        when(repository.findAll()).thenReturn(Flux.just(loanTypeEntity));
        Flux<LoanType> loanTypeFlux = adapter.findAllLoanTypes();
        StepVerifier.create(loanTypeFlux)
                .expectNext(loanType)
                .verifyComplete();
    }

    @Test
    void shouldThrowNotFoundWhenFindLoanTypeByAmountNotExist() {
        BigDecimal amount = new BigDecimal("9999.99");
        when(repository.findLoanTypeByAmount(amount)).thenReturn(Mono.empty());
        StepVerifier.create(adapter.findLoanTypeByMinAndMaxAmount(amount))
                .expectError(NotFoundException.class)
                .verify();
    }
}