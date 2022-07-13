package com.etnetera.hr.startup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Spring Boot application class.
 *
 * @author Etnetera
 *
 */
@SpringBootApplication
@ComponentScan("com.etnetera.hr.*")
@EntityScan("com.etnetera.hr.*")
@EnableJpaRepositories(basePackages = "com.etnetera.hr.repository")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

}
