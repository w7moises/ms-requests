package co.com.bancolombia.consumer;

import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateways.UserRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RestConsumer implements UserRepository {
    private final WebClient client;

    @CircuitBreaker(name = "findUserByDocumentNumber" /*, fallbackMethod = "testGetOk"*/)
    @Override
    public Mono<User> findUserByDocumentNumber(String documentNumber) {
        return client
                .get()
                .uri("/api/v1/users/document/{documentNumber}", documentNumber)
                .retrieve()
                .bodyToMono(User.class);
    }
}
