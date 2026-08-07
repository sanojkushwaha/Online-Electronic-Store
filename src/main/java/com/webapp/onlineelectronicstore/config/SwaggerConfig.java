package com.webapp.onlineelectronicstore.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "Bearer Authentication";
        return new OpenAPI()
                // JWT Security
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .schemaRequirement(securitySchemeName,
                        new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                )

                // API Information
                .info(new Info()
                        .title("Electronic Store Backend: APIs")
                        .version("1.0.0V")
                        .description("""
                                REST APIs for Online Electronic Store Created by Sanoj Kushwaha.
                                
                                Features:
                                • User Authentication (JWT)
                                • Google Login
                                • Category Management
                                • Product Management
                                • Cart Management
                                • Order Management
                                • User Management
                                """)
                        .contact(new Contact()
                                .name("Sanoj Kushwaha")
                                .email("kushawahasanoj123@gmail.com")
                                .url("https://github.com/sanojkushwaha"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")));
    }
}