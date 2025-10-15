package ar.edu.iua.iw3.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
@OpenAPIDefinition(
       security = @SecurityRequirement(name = "Bearer Authentication")
)
@SecurityScheme(
		  name = "Bearer Authentication",
		  type = SecuritySchemeType.HTTP,
		  bearerFormat = "JWT",
		  scheme = "bearer",
	      in = SecuritySchemeIn.HEADER
		)
public class OpenApiConfig {
 ///Doc: https://springdoc.org/#Introduction
   @Bean
   public OpenAPI openApi() {
       return new OpenAPI()
               .info(new Info()
                       .title("Backend - Ing Web 3")
                       .description("API Backend - Ing Web 3")
                       .version("v1.0")
                       .contact(new Contact()
                               .name("magm")
                               .url("https://github.com/magm3333")
                               .email("magm@iua.edu.ar"))
                       .termsOfService("TOC")
                       .license(new License().name("License").url("#"))
               );
   }
}

