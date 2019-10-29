package com.codecool.jpaintro.repository;

import com.codecool.jpaintro.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {

}