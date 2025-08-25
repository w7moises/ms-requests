package co.com.bancolombia.r2dbc.exception;

import org.springframework.http.HttpStatus;

public class FoundException extends I18nResponseStatusException {
    public FoundException(String code, Object... args) {
        super(HttpStatus.CONFLICT, code, args);
    }
}