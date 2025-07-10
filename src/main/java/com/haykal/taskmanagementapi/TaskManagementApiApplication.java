package com.haykal.taskmanagementapi;

import com.haykal.taskmanagementapi.dto.RegisterRequest;
import com.haykal.taskmanagementapi.service.AuthenticationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDate;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class TaskManagementApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskManagementApiApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(
            AuthenticationService service
    ) {
        return args -> {
            try {
                var admin = new RegisterRequest(
                        "Admin",
                        "Admin",
                        "admin@mail.com",
                        "password",
                        LocalDate.of(1990, 1, 1)
                );
                System.out.println("Admin token: " + service.register(admin).getAccessToken());
            } catch (RuntimeException e) {
                System.out.println("Admin user already exists or error occurred: " + e.getMessage());
            }

            try {
                var manager = new RegisterRequest(
                        "Manager",
                        "Manager",
                        "manager@mail.com",
                        "password",
                        LocalDate.of(1985, 5, 15)
                );
                System.out.println("Manager token: " + service.register(manager).getAccessToken());
            } catch (RuntimeException e) {
                System.out.println("Manager user already exists or error occurred: " + e.getMessage());
            }
        };
    }

}
