package com.codecool.jpaintro.repository;

import com.codecool.jpaintro.entity.Address;
import com.codecool.jpaintro.entity.Location;
import com.codecool.jpaintro.entity.School;
import com.codecool.jpaintro.entity.Student;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.assertj.core.api.Assertions.*;


import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertThrows;


@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
class AllRepositoryTest {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    SchoolRepository schoolRepository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    public void saveOneSimple() {
        Student john = Student.builder().name("john").email("j@j.com").build();
        studentRepository.save(john);

        List<Student> students = studentRepository.findAll();
        Assertions.assertThat(students).hasSize(1);
    }

    @Test
    public void saveUniqueFieldTwice() {
        Student john = Student.builder().name("john").email("j@j.com").build();
        Student john2 = Student.builder().name("john2").email("j@j.com").build();
        studentRepository.save(john);

        assertThrows(DataIntegrityViolationException.class, () -> studentRepository.saveAndFlush(john2));
    }

    @Test
    public void emailShouldNotBeNull() {
        Student john = Student.builder().name("john").build();

        assertThrows(DataIntegrityViolationException.class, () -> studentRepository.saveAndFlush(john));
    }

    @Test
    public void transientIsNotSaved() {
        Student john = Student.builder()
                .name("john")
                .email("j@j.com")
                .birthday(LocalDate.of(1982, 07, 24))
                .build();

        john.calculateAge();
        Assertions.assertThat(john.getAge()).isGreaterThanOrEqualTo(37);
        studentRepository.save(john);
        entityManager.clear();

        List<Student> students = studentRepository.findAll();
        Assertions.assertThat(students).allMatch(student ->  student.getAge() == 0L);
    }


    @Test
    public void addressPersistedWithStudent() {
        Address codecool = Address.builder()
                .country("Hungary")
                .city("Budapest")
                .address("Nagymező 44")
                .zipCode(1065)
                .build();

        Student john = Student.builder()
                .name("john")
                .email("j@j.com")
                .birthday(LocalDate.of(1982, 07, 24))
                .address(codecool)
                .build();


        studentRepository.save(john);

        List<Address> addresses = addressRepository.findAll();
        Assertions.assertThat(addresses).hasSize(1).allMatch(address -> address.getId() > 0L);
    }

    @Test
    public void studentsArePersistedAndDeletedWithSchool() {

        Set<Student> students = IntStream.range(1, 11)
                .boxed()
                .map(integer -> Student.builder().email("student" + integer + "@cc.com").build())
                .collect(Collectors.toSet());

        School school = School.builder()
                .name("CC")
                .location(Location.BUDAPEST)
                .students(students)
                .build();

        schoolRepository.saveAndFlush(school);

        List<School> schools = schoolRepository.findAll();
        Assertions.assertThat(schools).hasSize(1);

        List<Student> studentsFromDb = studentRepository.findAll();
        Assertions.assertThat(studentsFromDb)
                .hasSize(10)
                .anyMatch(student -> student.getEmail().equals("student5@cc.com"));

        schoolRepository.delete(school);
        Assertions.assertThat(studentRepository.findAll()).hasSize(0);
    }

    @Test
    public void findByNameStartingWithOrBirthdayBetween() {
        Student john = Student.builder()
                .name("john")
                .email("j@j.com")
                .birthday(LocalDate.of(1982, 07, 24))
                .build();

        Student joseph = Student.builder()
                .name("joseph")
                .email("jo@j.com")
                .birthday(LocalDate.of(1982, 07, 25))
                .build();

        Student laci = Student.builder()
                .name("laci")
                .email("l@j.com")
                .birthday(LocalDate.of(1982, 07, 24))
                .build();

        studentRepository.saveAll(Lists.newArrayList(john, joseph, laci));

        List<Student> jos = studentRepository.findByNameStartingWithAndBirthdayBetween(
                "jo",
                LocalDate.of(1982, 07, 24),
                LocalDate.of(1982, 07, 25)
        );

        Assertions.assertThat(jos).hasSize(2).allMatch(student -> student.getName().startsWith("jo"));
    }

    @Test
    public void testGetAllCountry() {
        Student john = Student.builder()
                .email("j@j.com")
                .address(Address.builder().country("HUN").build())
                .build();

        Student john2 = Student.builder()
                .email("j2@j.com")
                .address(Address.builder().country("HUN").build())
                .build();

        Student john3 = Student.builder()
                .email("j3@j.com")
                .address(Address.builder().country("ROU").build())
                .build();

        studentRepository.saveAll(Lists.newArrayList(john, john2, john3));

        List<String> countries = studentRepository.findAllCountry();

        Assertions.assertThat(countries).hasSize(2).containsExactly("HUN", "ROU");
    }
}