package co.com.bancolombia.r2dbc.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Getter
public abstract class I18nResponseStatusException extends ResponseStatusException {
    private final String code;
    private final Object[] args;

    protected I18nResponseStatusException(HttpStatus status, String code, Object... args) {
        super(status, code);
        this.code = code;
        this.args = args == null ? new Object[0] : args;
    }
}
