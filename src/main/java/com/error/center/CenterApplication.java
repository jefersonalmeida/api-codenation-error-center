package com.error.center;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.SpringDocUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

import java.awt.print.Pageable;
import java.util.Arrays;

@SpringBootApplication
@EnableCaching
public class CenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(CenterApplication.class, args);
    }

    @Bean
    public OpenAPI springEventLogOpenAPI() {
        SpringDocUtils.getConfig().replaceWithClass(org.springframework.data.domain.Pageable.class, Pageable.class);
        return new OpenAPI()
                .info(new Info().title("API Central de Erro")
                        .description("API criada para o projeto prático da Codenation")
                        .version("v1.0.0")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org"))
                )
                .components(new Components().addSecuritySchemes("TOKEN", new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .in(SecurityScheme.In.HEADER)
                        .name("Authorization")
                ))
                .addSecurityItem(new SecurityRequirement().addList("TOKEN", Arrays.asList("read", "write")));

    }
}
