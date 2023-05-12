package com.simplejourney.apidocs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "OpenAPI Demo APIs", version = "v0"))
public class ApidocsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApidocsApplication.class, args);
	}

}
