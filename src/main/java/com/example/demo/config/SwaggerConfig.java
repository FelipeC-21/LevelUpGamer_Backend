package com.example.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API REST - Level-Up Gamer")
                        .version("v1.0")
                        .description("Documentación oficial de los Endpoints del Backend para la tienda Level-Up Gamer. Proyecto de Evaluación 3 (Duoc UC).")
                        .contact(new Contact()
                                .name("Equipo Level-Up Gamer")
                                .email("contacto@levelupgamer.cl")));
    }
}