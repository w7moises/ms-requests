package co.com.bancolombia.usecase.loanpetition;

import co.com.bancolombia.model.loanpetition.LoanPetition;
import co.com.bancolombia.model.loanpetition.gateways.LoanPetitionRepository;
import co.com.bancolombia.model.loanpetition.gateways.SqsGateway;
import co.com.bancolombia.model.loantype.LoanType;
import co.com.bancolombia.model.loantype.gateways.LoanTypeRepository;
import co.com.bancolombia.model.response.*;
import co.com.bancolombia.model.state.State;
import co.com.bancolombia.model.state.gateways.StateRepository;
import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateways.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoanPetitionUseCaseTest {

    @Mock
    LoanPetitionRepository loanPetitionRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    StateRepository stateRepository;

    @Mock
    SqsGateway sqsGateway;

    @Mock
    LoanTypeRepository loanTypeRepository;

    LoanPetitionUseCase useCase;

    private static final User USER_WALTER = User.builder()
            .id(7L).name("Walter").lastName("Molina").salary(BigDecimal.valueOf(666L))
            .email("walter@acme.com").documentNumber("73727173")
            .build();

    private static final State STATE_PENDIENTE = State.builder()
            .id(100L).name("PENDIENTE DE REVISION").description("estado inicial")
            .build();

    private static final LoanType LOAN_MICRO = LoanType.builder()
            .id(10L).name("MICRO")
            .minAmount(new BigDecimal("100.00"))
            .maxAmount(new BigDecimal("2000.00"))
            .interestRate(new BigDecimal("0.12"))
            .automaticValidation(Boolean.TRUE)
            .build();

    private static final LoanPetition PETITION_NEW = LoanPetition.builder()
            .amount(new BigDecimal("1500.00"))
            .term(12)
            .documentNumber("73727173")
            .build();

    private static final LoanPetition PETITION_SAVED = LoanPetition.builder()
            .id(99L)
            .amount(new BigDecimal("1500.00"))
            .term(12)
            .email("walter@acme.com")
            .documentNumber("73727173")
            .stateId(100L)
            .loanTypeId(10L)
            .build();

    private static final PageDto pageDto = PageDto.builder()
            .number(0)
            .size(5)
            .totalElements(10L)
            .totalPages(2)
            .build();

    private static final List<DataGroupDto> data = List.of(
            DataGroupDto.builder()
                    .user(UserDto.builder()
                            .documentNumber("73727173")
                            .name("Walter")
                            .lastName("Molina")
                            .email("walter@acme.com")
                            .salary(BigDecimal.valueOf(666))
                            .build())
                    .loanPetitions(List.of(
                            PetitionItemDto.builder()
                                    .id(1L)
                                    .amount(BigDecimal.valueOf(666L))
                                    .term(3)
                                    .email("w@gmail.com")
                                    .documentNumber("73727173")
                                    .loanPetitionType("PRESTAMO1")
                                    .interestRate(BigDecimal.TEN)
                                    .loanPetitionState("APROBADO")
                                    .build()
                    ))
                    .totalMonthlyDebt(BigDecimal.ZERO)
                    .build()
    );

    private static final PagedDataResponse pagedDataResponse = new PagedDataResponse(pageDto, data);

    private static final LoanPetitionResponse loanPetitionResponse = new LoanPetitionResponse(1L, BigDecimal.valueOf(666L), 3, "w@gmail.com", "73727173", "PRESTAMO1",
            BigDecimal.TEN, "APROBADO", BigDecimal.ZERO);

    @BeforeEach
    void setUp() {
        useCase = new LoanPetitionUseCase(loanPetitionRepository, userRepository, stateRepository, loanTypeRepository, sqsGateway);
    }

    @Test
    void shouldSavePetition() {
        when(loanTypeRepository.findLoanTypeByMinAndMaxAmount(new BigDecimal("1500.00")))
                .thenReturn(Mono.just(LOAN_MICRO));
        when(loanPetitionRepository.findAllPetitionsByDocumentNumber(anyString()))
                .thenReturn(Flux.just(PETITION_SAVED));
        when(userRepository.findUserByDocumentNumber("73727173"))
                .thenReturn(Mono.just(USER_WALTER));
        when(stateRepository.findAllStates())
                .thenReturn(Flux.just(STATE_PENDIENTE));
        when(sqsGateway.sendInfoToLambdaDebtCapacity(anyList()))
                .thenReturn(Mono.just(true));
        when(loanPetitionRepository.savePetition(PETITION_NEW))
                .thenReturn(Mono.just(PETITION_SAVED));
        StepVerifier.create(useCase.savePetition(PETITION_NEW))
                .expectNext(PETITION_SAVED)
                .verifyComplete();
    }

    @Test
    void shouldFindAllPetitions() {
        when(loanPetitionRepository.findAllPetitions())
                .thenReturn(Flux.just(PETITION_SAVED));
        StepVerifier.create(useCase.findAllPetitions())
                .expectNext(PETITION_SAVED)
                .verifyComplete();
    }

    @Test
    void shouldFindAllPetitionsByEmail() {
        when(loanPetitionRepository.findAllPetitionsByEmail("walter@acme.com"))
                .thenReturn(Flux.just(PETITION_SAVED));
        StepVerifier.create(useCase.findAllPetitionsByEmail("walter@acme.com"))
                .expectNext(PETITION_SAVED)
                .verifyComplete();
    }

    @Test
    void shouldFindAllPetitionsByDocumentNumber() {
        when(loanPetitionRepository.findAllPetitionsByDocumentNumber("73727173"))
                .thenReturn(Flux.just(PETITION_SAVED));
        StepVerifier.create(useCase.findAllPetitionsByDocumentNumber("73727173"))
                .expectNext(PETITION_SAVED)
                .verifyComplete();
    }

    @Test
    void shouldFindPetitionsFiltered() {
        when(loanPetitionRepository.findLoanPetitionsPageFiltered(null, null, null, 5, 0))
                .thenReturn(Flux.just(loanPetitionResponse));
        when(userRepository.findUserByDocumentNumber("73727173")).thenReturn(Mono.just(USER_WALTER));
        when(loanPetitionRepository.countFiltered(null, null, null)).thenReturn(Mono.just(10L));
        StepVerifier.create(useCase.findAllPetitionsFiltered(null, null, null, 0, 5))
                .expectNext(pagedDataResponse)
                .verifyComplete();
    }
}