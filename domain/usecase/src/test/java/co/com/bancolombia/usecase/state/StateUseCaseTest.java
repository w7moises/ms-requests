package co.com.bancolombia.usecase.state;


import co.com.bancolombia.model.state.State;
import co.com.bancolombia.model.state.gateways.StateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StateUseCaseTest {
    @Mock
    StateRepository repository;

    StateUseCase stateUseCase;

    private static final State STATE_ACTIVO = State.builder()
            .id(1L)
            .name("ACTIVO")
            .description("ACTIVO")
            .build();

    private static final State STATE_PENDIENTE = State.builder()
            .id(2L)
            .name("PENDIENTE")
            .description("PENDIENTE")
            .build();

    private static final State STATE_NUEVO = State.builder()
            .name("NUEVO")
            .description("NUEVO")
            .build();

    @BeforeEach
    void setUp() {
        stateUseCase = new StateUseCase(repository);
    }

    @Test
    void shouldFindStateById() {
        when(repository.findStateById(1L)).thenReturn(Mono.just(STATE_ACTIVO));
        StepVerifier.create(stateUseCase.findStateById(1L))
                .expectNext(STATE_ACTIVO)
                .verifyComplete();
    }

    @Test
    void shouldFindAllStates() {
        when(repository.findAllStates()).thenReturn(Flux.just(STATE_ACTIVO, STATE_PENDIENTE));
        StepVerifier.create(stateUseCase.findAllStates())
                .expectNext(STATE_ACTIVO)
                .expectNext(STATE_PENDIENTE)
                .verifyComplete();
    }

    @Test
    void shouldSaveState() {
        State saved = STATE_NUEVO.toBuilder().id(2L).build();
        when(repository.saveState(STATE_NUEVO)).thenReturn(Mono.just(saved));
        StepVerifier.create(stateUseCase.saveState(STATE_NUEVO))
                .expectNext(saved)
                .verifyComplete();
    }

    @Test
    void shouldUpdateState() {
        State updated = STATE_ACTIVO.toBuilder().description("CAMBIO").build();
        when(repository.updateState(updated)).thenReturn(Mono.just(updated));
        StepVerifier.create(stateUseCase.updateState(updated))
                .expectNext(updated)
                .verifyComplete();
    }
}
