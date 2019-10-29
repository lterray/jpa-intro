package com.codecool.jpaintro.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Student {

    @GeneratedValue
    @Id
    private Long id;

    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    private LocalDate birthday;

    @Transient
    private long age;

    @ElementCollection
    @Singular
    private List<String> phoneNumbers;

    @OneToOne(cascade = CascadeType.PERSIST)
    private Address address;

    @ManyToOne
    private School school;

    public void calculateAge() {
        if (birthday != null) {
            age = ChronoUnit.YEARS.between(birthday, LocalDate.now());
        }
    }
}
