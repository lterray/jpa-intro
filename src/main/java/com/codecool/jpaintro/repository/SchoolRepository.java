package com.codecool.jpaintro.repository;

import com.codecool.jpaintro.entity.School;
import com.codecool.jpaintro.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<School, Long> {

}