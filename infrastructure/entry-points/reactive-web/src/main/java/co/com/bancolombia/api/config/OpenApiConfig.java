package co.com.bancolombia.api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Pragma Template API",
                version = "1.0.0",
                description = "API for Pragma Template Application",
                contact = @Contact(
                        name = "Pragma Team",
                        email = "support@pragma.co",
                        url = "https://pragma.co"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0"
                )
        )
)
public class OpenApiConfig {
    @Bean
    public OpenAPI api() {
        Schema<?> apiError = new ObjectSchema().properties(Map.of(
                "timestamp", new StringSchema()
                        .format("date-time")
                        .example("2025-08-23T16:23:16.107+00:00"),
                "path", new StringSchema()
                        .example("/path"),
                "status", new StringSchema()
                        .example("status"),
                "error", new StringSchema()
                        .example("error"),
                "requestId", new StringSchema()
                        .example("4a8e2e37-1"),
                "message", new StringSchema()
                        .example("No static resource.")
        ));
        return new OpenAPI().components(new Components().addSchemas("ApiError", apiError));
    }
}
