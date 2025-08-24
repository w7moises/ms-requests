package co.com.bancolombia.r2dbc.repository;

import co.com.bancolombia.model.state.State;
import co.com.bancolombia.model.state.gateways.StateRepository;
import co.com.bancolombia.r2dbc.entity.StateEntity;
import co.com.bancolombia.r2dbc.exception.NotFoundException;
import co.com.bancolombia.r2dbc.helper.ReactiveAdapterOperations;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@Slf4j
public class StateReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        State,
        StateEntity,
        Long,
        StateReactiveRepository
        > implements StateRepository {
    public StateReactiveRepositoryAdapter(StateReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, State.class));
    }

    @Transactional(transactionManager = "r2dbcTransactionManager", readOnly = true)
    @Override
    public Mono<State> findStateById(Long id) {
        return super.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("state.notFound.id", id)));
    }

    @Transactional(transactionManager = "r2dbcTransactionManager", readOnly = true)
    @Override
    public Flux<State> findAllStates() {
        return super.findAll();
    }


    @Transactional(transactionManager = "r2dbcTransactionManager")
    @Override
    public Mono<State> saveState(State state) {
        return super.save(state);
    }


    @Transactional(transactionManager = "r2dbcTransactionManager")
    @Override
    public Mono<State> updateState(State state) {
        return super.findById(state.getId())
                .switchIfEmpty(Mono.error(new NotFoundException("state.notFound.id", state.getId())))
                .flatMap(data -> super.save(state));
    }
}
