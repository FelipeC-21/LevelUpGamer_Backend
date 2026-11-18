package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Esta anotación es la magia de Spring Boot: auto-configura el proyecto, 
// escanea nuestros controladores/servicios y levanta el servidor web.
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
        System.out.println("=========================================================");
        System.out.println("🚀 BACKEND LEVEL-UP GAMER INICIADO CON ÉXITO EN EL PUERTO 8080 🚀");
        System.out.println("=========================================================");
    }

}