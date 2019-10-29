package com.codecool.jpaintro.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Address {

    @GeneratedValue
    @Id
    private Long id;

    private String country;
    private String city;
    private String address;

    private int zipCode;

    @OneToOne(mappedBy = "address")
    @EqualsAndHashCode.Exclude
    private Student student;

}
