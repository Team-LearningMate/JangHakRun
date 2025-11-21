package com.learning_mate.janghakrun.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
            title = "장학런 API",
            description = "장학런 백엔드 API 명세",
            version = "v1"
        )
)
@Configuration
public class SwaggerConfig {
    private static final String SECURITY_SCHEME_NAME = "JWT"; // Swagger UI에서 보이는 이름
    private static final String BEARER_FORMAT = "JWT"; // bearer 토큰 포맷 설정

    @Bean
    public OpenAPI openAPI() {
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(SECURITY_SCHEME_NAME);

        Components components = new Components()
                .addSecuritySchemes(SECURITY_SCHEME_NAME, new SecurityScheme()
                        .name(SECURITY_SCHEME_NAME)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat(BEARER_FORMAT));

        return new OpenAPI()
                .addSecurityItem(securityRequirement)
                .components(components);
    }
}
