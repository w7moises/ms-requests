package co.com.bancolombia.api.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.i18n.AcceptHeaderLocaleContextResolver;
import org.springframework.web.server.i18n.LocaleContextResolver;

import java.util.Locale;

@Configuration
public class I18nConfig {

    @Bean
    public MessageSource messageSource() {
        var message = new ReloadableResourceBundleMessageSource();
        message.setBasename("classpath:messages");
        message.setDefaultEncoding("UTF-8");
        message.setFallbackToSystemLocale(false);
        message.setUseCodeAsDefaultMessage(true);
        return message;
    }

    @Bean
    public LocaleContextResolver localeContextResolver() {
        var resolver = new AcceptHeaderLocaleContextResolver();
        resolver.setDefaultLocale(Locale.forLanguageTag("es"));
        return resolver;
    }

    @Bean
    public WebFilter localeParamWebFilter(LocaleContextResolver resolver) {
        return (exchange, chain) -> {
            var queryParam = exchange.getRequest().getQueryParams().getFirst("lang");
            if (queryParam != null && !queryParam.isBlank()) {
                var locale = Locale.forLanguageTag(queryParam);
                exchange.getAttributes().put(HttpHeaders.ACCEPT_LANGUAGE, locale.toLanguageTag());
            }
            return chain.filter(exchange);
        };
    }
}
