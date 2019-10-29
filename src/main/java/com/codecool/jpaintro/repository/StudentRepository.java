package com.codecool.jpaintro.repository;

import com.codecool.jpaintro.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByNameStartingWithAndBirthdayBetween(String namePart,
                                                          LocalDate from,
                                                          LocalDate to);

    @Query("SELECT s.address.country FROM Student s GROUP BY s.address.country")
    List<String> findAllCountry();

//    @Modifying
//    @Query("UPDATE student set age = :age where name = :name")
//    int updateStudentSetStatusForName(@Param("age") Integer age,
//                                      @Param("name") String name);

}