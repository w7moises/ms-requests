package co.com.bancolombia.api;

import co.com.bancolombia.model.state.State;
import co.com.bancolombia.usecase.state.StateUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class StateHandler {
    private final StateUseCase stateUseCase;

    public Mono<ServerResponse> getAllStates(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(stateUseCase.findAllStates(), State.class);
    }

    public Mono<ServerResponse> findStateById(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));
        return stateUseCase.findStateById(id)
                .flatMap(state -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(state));
    }

    public Mono<ServerResponse> createState(ServerRequest request) {
        return request.bodyToMono(State.class)
                .flatMap(stateUseCase::saveState)
                .flatMap(state -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(state));
    }

    public Mono<ServerResponse> updateState(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));
        return request.bodyToMono(State.class)
                .flatMap(data -> {
                    data.setId(id);
                    return stateUseCase.updateState(data)
                            .flatMap(state -> ServerResponse.ok()
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .bodyValue(state));
                });
    }
}
