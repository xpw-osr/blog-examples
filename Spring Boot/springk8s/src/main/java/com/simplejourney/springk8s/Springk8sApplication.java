package com.simplejourney.springk8s;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "OpenAPI Demo APIs", version = "v0"))
public class Springk8sApplication {

	public static void main(String[] args) {
		SpringApplication.run(Springk8sApplication.class, args);
	}

}
