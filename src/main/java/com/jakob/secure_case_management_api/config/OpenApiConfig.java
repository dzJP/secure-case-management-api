package com.jakob.secure_case_management_api.config;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.*;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Secure Case Management API")
                        .version("1.0")
                        .description("Spring Boot backend with JWT authentication, role-based access, audit logs, and soft delete"));
    }
}