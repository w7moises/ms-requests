package co.com.bancolombia.usecase.state;

import co.com.bancolombia.model.state.State;
import co.com.bancolombia.model.state.gateways.StateRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class StateUseCase {
    private final StateRepository stateRepository;

    public Mono<State> findStateById(Long id) {
        return stateRepository.findStateById(id);
    }

    public Flux<State> findAllStates() {
        return stateRepository.findAllStates();
    }

    public Mono<State> saveState(State state) {
        return stateRepository.saveState(state);
    }

    public Mono<State> updateState(State state) {
        return stateRepository.updateState(state);
    }
}
