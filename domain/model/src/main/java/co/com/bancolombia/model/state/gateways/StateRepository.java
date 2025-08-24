package co.com.bancolombia.model.state.gateways;

import co.com.bancolombia.model.state.State;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StateRepository {
    Mono<State> findStateById(Long id);

    Flux<State> findAllStates();

    Mono<State> saveState(State state);

    Mono<State> updateState(State state);
}
