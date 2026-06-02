package org.fp.stamcam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class StamCamBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(StamCamBackendApplication.class, args);
    }

}

