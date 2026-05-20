package org.fp.stamcam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Spring Boot Application class for StamCam Backend.
 * Initializes the Spring Boot application with MongoDB and Swagger support.
 */
@SpringBootApplication
public class StamCamBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(StamCamBackendApplication.class, args);
    }

}

