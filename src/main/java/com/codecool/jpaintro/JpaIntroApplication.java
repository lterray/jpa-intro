package com.codecool.jpaintro;

import com.codecool.jpaintro.entity.Address;
import com.codecool.jpaintro.entity.Student;
import com.codecool.jpaintro.repository.AddressRepository;
import com.codecool.jpaintro.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.time.LocalDate;

@SpringBootApplication
public class JpaIntroApplication {

    @Autowired
    private StudentRepository studentRepository;

    public static void main(String[] args) {
        SpringApplication.run(JpaIntroApplication.class, args);
    }

    @Bean
    @Profile("production")
    public CommandLineRunner init() {
        return args -> {
            Student laci = Student.builder()
                    .email("laci@codecool.com")
                    .name("Laci")
                    .birthday(LocalDate.of(1982, 7, 24))
                    .address(Address.builder().country("Hungary").zipCode(1000).build())
                    .phoneNumber("12345678")
                    .phoneNumber("87654321")
                    .build();

            laci.calculateAge();
            studentRepository.save(laci);
        };
    }
}
