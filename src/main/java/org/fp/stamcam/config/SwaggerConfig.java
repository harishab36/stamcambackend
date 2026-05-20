package org.fp.stamcam.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger/OpenAPI configuration class.
 * Customizes the API documentation displayed in Swagger UI.
 */
@Configuration
public class SwaggerConfig {

    /**
     * Configure OpenAPI documentation.
     *
     * @return configured OpenAPI object
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("StamCam Backend API")
                        .version("1.0.0")
                        .description("Spring Boot REST API for Camera Management System")
                        .contact(new Contact()
                                .name("Development Team")
                                .url("https://github.com/fp")
                                .email("dev@stamcam.local"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }

}

