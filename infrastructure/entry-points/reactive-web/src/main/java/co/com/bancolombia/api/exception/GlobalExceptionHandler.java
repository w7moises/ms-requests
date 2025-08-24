package co.com.bancolombia.api.exception;

import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.lang.reflect.Method;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
public class GlobalExceptionHandler extends AbstractErrorWebExceptionHandler {

    private final MessageSource messageSource;

    public GlobalExceptionHandler(
            ErrorAttributes errorAttributes,
            WebProperties webProperties,
            ApplicationContext applicationContext,
            ServerCodecConfigurer configurer,
            MessageSource messageSource
    ) {
        super(errorAttributes, webProperties.getResources(), applicationContext);
        this.setMessageWriters(configurer.getWriters());
        this.setMessageReaders(configurer.getReaders());
        this.messageSource = messageSource;
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        ErrorAttributeOptions options = ErrorAttributeOptions.defaults()
                .including(ErrorAttributeOptions.Include.MESSAGE);

        Map<String, Object> errorProps = getErrorAttributes(request, options);
        Throwable ex = getError(request);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        Object s = errorProps.get("status");
        if (s instanceof Integer i) status = HttpStatus.valueOf(i);
        Locale locale = resolveLocale(request);
        String currentMessage = String.valueOf(errorProps.getOrDefault("message", "")).trim();
        var i18n = extractI18n(ex).or(() -> extractI18n(ex != null ? ex.getCause() : null));
        String resolvedMessage = currentMessage;

        if (i18n.isPresent()) {
            var p = i18n.get();
            resolvedMessage = messageSource.getMessage(p.code, p.args, p.code, locale);
        } else if (ex instanceof ResponseStatusException rse) {
            String reason = Optional.ofNullable(rse.getReason()).orElse("").trim();
            if (!reason.isBlank()) {
                resolvedMessage = messageSource.getMessage(reason, null, reason, locale);
            } else if (resolvedMessage.isBlank()) {
                resolvedMessage = ex.getMessage();
            }
        } else if (resolvedMessage.isBlank() && ex != null && ex.getMessage() != null) {
            resolvedMessage = ex.getMessage();
        }
        errorProps.put("message", resolvedMessage);
        return ServerResponse.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(errorProps));
    }

    private Locale resolveLocale(ServerRequest request) {
        String queryParam = request.queryParam("lang").orElse(null);
        if (queryParam != null && !queryParam.isBlank()) {
            return Locale.forLanguageTag(queryParam);
        }
        return request.exchange().getLocaleContext().getLocale();
    }

    private Optional<I18nPayload> extractI18n(Throwable ex) {
        if (ex == null) return Optional.empty();
        try {
            Method messageCode = ex.getClass().getMethod("getCode");
            Method messageArgs = ex.getClass().getMethod("getArgs");
            String code = String.valueOf(messageCode.invoke(ex));
            Object[] args = (Object[]) messageArgs.invoke(ex);
            return Optional.of(new I18nPayload(code, args != null ? args : new Object[0]));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private record I18nPayload(String code, Object[] args) {
    }
}
