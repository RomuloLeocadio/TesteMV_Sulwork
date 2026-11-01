package com.mv.teste.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI().info(new Info()
                .title("API de Eventos de Alimentação")
                .description("Organização de eventos, participantes, itens, contribuições, cobertura e alertas de alérgenos.")
                .version("1.0"));
    }
}