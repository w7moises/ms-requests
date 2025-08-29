package co.com.bancolombia.api.utils;

import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.jwt.Jwt;
import reactor.core.publisher.Mono;

import java.util.Optional;

public final class TokenInformationMapper {
    public static final String CLAIM_DOCUMENT = "document";

    private TokenInformationMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static Mono<Jwt> currentJwt() {
        return ReactiveSecurityContextHolder.getContext()
                .switchIfEmpty(Mono.error(new RuntimeException("Security context is empty")))
                .map(SecurityContext::getAuthentication)
                .filter(auth -> auth != null && auth.getPrincipal() instanceof Jwt)
                .switchIfEmpty(Mono.error(new RuntimeException("JWT principal not found")))
                .map(auth -> (Jwt) auth.getPrincipal());
    }

    public static Mono<String> getRequiredStringClaim(String claimName, String errorIfMissing) {
        return currentJwt()
                .map(jwt -> Optional.ofNullable(jwt.getClaimAsString(claimName)))
                .flatMap(opt -> opt.map(Mono::just)
                        .orElseGet(() -> Mono.error(new RuntimeException(errorIfMissing))));
    }

    public static Mono<String> getDocumentNumberFromToken() {
        return getRequiredStringClaim(CLAIM_DOCUMENT, "No document found in token");
    }
}
