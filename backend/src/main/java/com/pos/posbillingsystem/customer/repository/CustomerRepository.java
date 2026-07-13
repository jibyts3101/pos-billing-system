package com.pos.posbillingsystem.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pos.posbillingsystem.customer.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    boolean existsByPhone(String phone);

}