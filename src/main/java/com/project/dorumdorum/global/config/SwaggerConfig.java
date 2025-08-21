package com.project.dorumdorum.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        String schemeName = "Bearer Authentication";

        Components components = new Components()
                .addSecuritySchemes(schemeName,
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                );

        SecurityRequirement requirement = new SecurityRequirement()
                .addList(schemeName);

        return new OpenAPI()
                .components(components)
                .addSecurityItem(requirement)
                .info(new Info()
                        .title("도룸도룸 API")
                        .description("API 문서")
                        .version("1.0.0")
                );
    }
}
