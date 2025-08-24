package co.com.bancolombia.r2dbc.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends I18nResponseStatusException {
    public NotFoundException(String code, Object... args) {
        super(HttpStatus.NOT_FOUND, code, args);
    }
}
