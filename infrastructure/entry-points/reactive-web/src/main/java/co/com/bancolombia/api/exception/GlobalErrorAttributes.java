package co.com.bancolombia.api.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {

    private final MessageSource messageSource;

    public GlobalErrorAttributes(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Map<String, Object> attrs = super.getErrorAttributes(
                request,
                options.including(ErrorAttributeOptions.Include.MESSAGE, ErrorAttributeOptions.Include.BINDING_ERRORS)
        );
        Throwable error = getError(request);
        Locale locale = resolveLocale(request);

        if (error instanceof ConstraintViolationException cve) {
            attrs.put("status", 400);
            attrs.put("error", "Bad Request");
            attrs.put("message", msg(locale, "validation.failed", "Validation failed"));
            List<Map<String, Object>> violations = cve.getConstraintViolations().stream()
                    .map(v -> errorEntry(
                            v.getPropertyPath() != null ? v.getPropertyPath().toString() : null,
                            resolveConstraintViolationMessage(v, locale),
                            v.getInvalidValue()
                    ))
                    .toList();
            attrs.put("errors", violations);

        } else if (error instanceof WebExchangeBindException web) {
            attrs.put("status", 400);
            attrs.put("error", "Bad Request");
            attrs.put("message", msg(locale, "binding.failed", "Binding failed"));

            List<Map<String, Object>> fieldErrors = web.getFieldErrors().stream()
                    .map(fe -> errorEntry(
                            fe.getField(),
                            resolveFieldError(fe, locale),
                            fe.getRejectedValue()
                    ))
                    .toList();

            List<Map<String, Object>> globalErrors = web.getGlobalErrors().stream()
                    .map(ge -> errorEntry(
                            ge.getObjectName(),
                            resolveObjectError(ge, locale),
                            null
                    ))
                    .toList();
            var all = new java.util.ArrayList<Map<String, Object>>(fieldErrors.size() + globalErrors.size());
            all.addAll(fieldErrors);
            all.addAll(globalErrors);
            attrs.put("errors", all);
        }
        return attrs;
    }

    private Locale resolveLocale(ServerRequest request) {
        String qp = request.queryParam("lang").orElse(null);
        if (qp != null && !qp.isBlank()) return Locale.forLanguageTag(qp);
        return request.exchange().getLocaleContext().getLocale();
    }

    private String msg(Locale locale, String code, String defaultMsg, Object... args) {
        try {
            return messageSource.getMessage(code, args, defaultMsg, locale);
        } catch (NoSuchMessageException e) {
            return defaultMsg;
        }
    }

    private String resolveFieldError(FieldError fe, Locale locale) {
        try {
            return messageSource.getMessage(fe, locale);
        } catch (NoSuchMessageException e) {
            return fe.getDefaultMessage() != null ? fe.getDefaultMessage() : "Invalid value";
        }
    }

    private String resolveObjectError(ObjectError oe, Locale locale) {
        try {
            return messageSource.getMessage(oe, locale);
        } catch (NoSuchMessageException e) {
            return oe.getDefaultMessage() != null ? oe.getDefaultMessage() : "Invalid request";
        }
    }

    private String resolveConstraintViolationMessage(ConstraintViolation<?> v, Locale locale) {
        String template = v.getMessageTemplate();
        if (template != null && template.startsWith("{") && template.endsWith("}")) {
            String code = template.substring(1, template.length() - 1);
            try {
                return messageSource.getMessage(code, null, code, locale);
            } catch (NoSuchMessageException ignored) {
            }
        }
        return v.getMessage();
    }

    private static Map<String, Object> errorEntry(String field, String message, Object rejectedValue) {
        var m = new LinkedHashMap<String, Object>(3);
        m.put("field", field);
        m.put("message", message);
        m.put("rejectedValue", rejectedValue);
        return m;
    }
}