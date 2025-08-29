package co.com.bancolombia.api.utils;

import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.jwt.Jwt;
import reactor.core.publisher.Mono;

public class TokenInformationMapper {
    public static Mono<String> getDocumentNumberFromToken() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(authentication -> {
                    Jwt jwt = (Jwt) authentication.getPrincipal();
                    return jwt.getClaimAsString("document");
                })
                .switchIfEmpty(Mono.error(new RuntimeException("No document found in token")));
    }
}
