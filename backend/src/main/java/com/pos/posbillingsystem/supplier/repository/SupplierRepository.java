package com.pos.posbillingsystem.supplier.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pos.posbillingsystem.supplier.entity.Supplier;

public interface SupplierRepository extends JpaRepository<Supplier, Integer> {

    boolean existsBySupplierName(String supplierName);

    Optional<Supplier> findBySupplierName(String supplierName);

}