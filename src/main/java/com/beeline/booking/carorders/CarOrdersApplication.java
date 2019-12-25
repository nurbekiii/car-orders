package com.beeline.booking.carorders;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.beeline.booking.carorders.repo")
@EnableTransactionManagement
@EntityScan(basePackages = "com.beeline.booking.carorders.entity")
@Configuration
@Import({ThymeleafConfiguration.class})
public class CarOrdersApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarOrdersApplication.class, args);
    }

}
