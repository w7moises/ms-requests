package co.com.bancolombia.r2dbc;

import co.com.bancolombia.model.state.State;
import co.com.bancolombia.r2dbc.entity.StateEntity;
import co.com.bancolombia.r2dbc.exception.NotFoundException;
import co.com.bancolombia.r2dbc.repository.StateReactiveRepository;
import co.com.bancolombia.r2dbc.repository.StateReactiveRepositoryAdapter;
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

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StateReactiveRepositoryAdapterTest {

    @InjectMocks
    private StateReactiveRepositoryAdapter adapter;

    @Mock
    private StateReactiveRepository repository;

    @Mock
    private ObjectMapper mapper;

    private StateEntity stateEntity;
    private State state;

    @BeforeEach
    void setUp() {
        stateEntity = new StateEntity(1L, "APROBADO", "Estado aprobado");
        state = new State(1L, "RECHAZADO", "Estado rechazado");
    }

    @Test
    void shouldSaveState() {
        when(mapper.map(state, StateEntity.class)).thenReturn(stateEntity);
        when(repository.save(stateEntity)).thenReturn(Mono.just(stateEntity));
        when(mapper.map(stateEntity, State.class)).thenReturn(state);
        StepVerifier.create(adapter.saveState(state))
                .expectNext(state)
                .verifyComplete();
    }

    @Test
    void shouldFindAllStates() {
        when(repository.findAll()).thenReturn(Flux.just(stateEntity));
        when(mapper.map(stateEntity, State.class)).thenReturn(state);
        StepVerifier.create(adapter.findAllStates())
                .expectNext(state)
                .verifyComplete();
    }

    @Test
    void shouldFindStateById() {
        when(repository.findById(1L)).thenReturn(Mono.just(stateEntity));
        when(mapper.map(stateEntity, State.class)).thenReturn(state);
        StepVerifier.create(adapter.findStateById(1L))
                .expectNext(state)
                .verifyComplete();
    }

    @Test
    void shouldThrowNotFoundWhenStateIdNotExist() {
        Long stateId = 999L;
        when(repository.findById(stateId)).thenReturn(Mono.empty());
        StepVerifier.create(adapter.findStateById(stateId))
                .expectErrorMatches(throwable -> throwable instanceof NotFoundException &&
                        throwable.getMessage().contains("state.notFound.id"))
                .verify();
    }
}