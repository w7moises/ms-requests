package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.loanpetition.CreateLoanPetitionDto;
import co.com.bancolombia.api.dto.loanpetition.LoanPetitionDto;
import co.com.bancolombia.api.dto.loantype.CreateLoanTypeDto;
import co.com.bancolombia.api.dto.loantype.LoanTypeDto;
import co.com.bancolombia.api.mapper.LoanPetitionDtoMapperImpl;
import co.com.bancolombia.api.mapper.LoanTypeDtoMapperImpl;
import co.com.bancolombia.model.loanpetition.LoanPetition;
import co.com.bancolombia.model.loantype.LoanType;
import co.com.bancolombia.model.state.State;
import co.com.bancolombia.usecase.loanpetition.LoanPetitionUseCase;
import co.com.bancolombia.usecase.loantype.LoanTypeUseCase;
import co.com.bancolombia.usecase.state.StateUseCase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {
        RouterRest.class,
        StateHandler.class,
        LoanTypeHandler.class,
        LoanPetitionHandler.class
})
@WebFluxTest(excludeAutoConfiguration = {
        org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration.class
})
@Import({LoanTypeDtoMapperImpl.class, LoanPetitionDtoMapperImpl.class})
class RouterRestTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private StateUseCase stateUseCase;

    @MockitoBean
    private LoanTypeUseCase loanTypeUseCase;

    @MockitoBean
    private LoanPetitionUseCase loanPetitionUseCase;
    
    private State stateMock;
    private State stateUpdated;
    private LoanType loanTypeMock;
    private LoanType loanTypeUpdated;
    private LoanPetition loanPetitionMock;
    private CreateLoanTypeDto createLoanTypeDto;
    private CreateLoanPetitionDto createLoanPetitionDto;

    @BeforeEach
    void setup() {
        stateMock = new State(1L, "APROBADO", "Estado aprobado");
        stateUpdated = new State(1L, "RECHAZADO", "Estado rechazado");
        loanTypeMock = LoanType.builder()
                .id(1L)
                .name("Personal")
                .minAmount(BigDecimal.valueOf(1000))
                .maxAmount(BigDecimal.valueOf(5000))
                .interestRate(BigDecimal.valueOf(5))
                .automaticValidation(true)
                .build();
        loanTypeUpdated = loanTypeMock.toBuilder().name("Consumo").build();
        loanPetitionMock = LoanPetition.builder()
                .id(1L)
                .amount(BigDecimal.valueOf(2500))
                .term(12)
                .email("test@mail.com")
                .documentNumber("12345678")
                .stateId(1L)
                .loanTypeId(1L)
                .build();

        createLoanTypeDto = new CreateLoanTypeDto(
                "Personal",
                BigDecimal.valueOf(1000),
                BigDecimal.valueOf(5000),
                BigDecimal.valueOf(5),
                true
        );

        createLoanPetitionDto = new CreateLoanPetitionDto(
                BigDecimal.valueOf(2500),
                12,
                "12345678"
        );
    }

    @Test
    void shouldGetAllStates() {
        when(stateUseCase.findAllStates()).thenReturn(Flux.just(stateMock));
        webTestClient.get()
                .uri("/api/v1/states")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(State.class)
                .hasSize(1);
    }

    @Test
    void shouldGetStateById() {
        when(stateUseCase.findStateById(1L)).thenReturn(Mono.just(stateMock));
        webTestClient.get()
                .uri("/api/v1/states/{id}", 1L)
                .exchange()
                .expectStatus().isOk()
                .expectBody(State.class)
                .value(state -> Assertions.assertThat(state.getName()).isEqualTo("APROBADO"));
    }

    @Test
    void shouldCreateState() {
        when(stateUseCase.saveState(any(State.class))).thenReturn(Mono.just(stateMock));
        webTestClient.post()
                .uri("/api/v1/states")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(stateMock)
                .exchange()
                .expectStatus().isOk()
                .expectBody(State.class)
                .value(state -> Assertions.assertThat(state.getId()).isEqualTo(1L));
    }

    @Test
    void shouldUpdateState() {
        when(stateUseCase.updateState(any(State.class))).thenReturn(Mono.just(stateUpdated));
        webTestClient.put()
                .uri("/api/v1/states/{id}", 1L)
                .bodyValue(stateUpdated)
                .exchange()
                .expectStatus().isOk()
                .expectBody(State.class)
                .value(state -> Assertions.assertThat(state.getName()).isEqualTo("RECHAZADO"));
    }

    @Test
    void shouldGetAllLoanTypes() {
        when(loanTypeUseCase.findAllLoanTypes()).thenReturn(Flux.just(loanTypeMock));
        webTestClient.get()
                .uri("/api/v1/loanTypes")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(LoanTypeDto.class)
                .hasSize(1);
    }

    @Test
    void shouldGetLoanTypeById() {
        when(loanTypeUseCase.findLoanTypeById(1L)).thenReturn(Mono.just(loanTypeMock));
        webTestClient.get()
                .uri("/api/v1/loanTypes/{id}", 1L)
                .exchange()
                .expectStatus().isOk()
                .expectBody(LoanTypeDto.class)
                .value(loanType -> Assertions.assertThat(loanType.name()).isEqualTo("Personal"));
    }

    @Test
    void shouldCreateLoanType() {
        when(loanTypeUseCase.saveLoanType(any(LoanType.class))).thenReturn(Mono.just(loanTypeMock));
        webTestClient.post()
                .uri("/api/v1/loanTypes")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createLoanTypeDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(LoanTypeDto.class)
                .value(loanType -> Assertions.assertThat(loanType.id()).isEqualTo(1L));
    }

    @Test
    void shouldUpdateLoanType() {
        when(loanTypeUseCase.updateLoanType(any(LoanType.class))).thenReturn(Mono.just(loanTypeUpdated));
        webTestClient.put()
                .uri("/api/v1/loanTypes/{id}", 1L)
                .bodyValue(loanTypeUpdated)
                .exchange()
                .expectStatus().isOk()
                .expectBody(LoanTypeDto.class)
                .value(loanType -> Assertions.assertThat(loanType.name()).isEqualTo("Consumo"));
    }

    @Test
    void shouldDeleteLoanType() {
        when(loanTypeUseCase.deleteLoanType(1L)).thenReturn(Mono.empty());
        webTestClient.delete()
                .uri("/api/v1/loanTypes/{id}", 1L)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void shouldGetAllLoanPetitions() {
        when(loanPetitionUseCase.findAllPetitions()).thenReturn(Flux.just(loanPetitionMock));
        webTestClient.get()
                .uri("/api/v1/loanPetitions")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(LoanPetitionDto.class)
                .hasSize(1);
    }

    @Test
    void shouldGetLoanPetitionsByEmail() {
        when(loanPetitionUseCase.findAllPetitionsByEmail("test@mail.com"))
                .thenReturn(Flux.just(loanPetitionMock));
        webTestClient.get()
                .uri("/api/v1/loanPetitions/email/{email}", "test@mail.com")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(LoanPetitionDto.class)
                .hasSize(1);
    }

    @Test
    void shouldGetLoanPetitionsByDocumentNumber() {
        when(loanPetitionUseCase.findAllPetitionsByDocumentNumber("12345678"))
                .thenReturn(Flux.just(loanPetitionMock));
        webTestClient.get()
                .uri("/api/v1/loanPetitions/document/{documentNumber}", "12345678")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(LoanPetitionDto.class)
                .hasSize(1);
    }

    @Test
    void shouldCreateLoanPetition() {
        when(loanPetitionUseCase.savePetition(any(LoanPetition.class)))
                .thenReturn(Mono.just(loanPetitionMock));
        webTestClient.post()
                .uri("/api/v1/loanPetitions")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createLoanPetitionDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(LoanPetitionDto.class)
                .value(petition -> Assertions.assertThat(petition.id()).isEqualTo(1L));
    }
}
