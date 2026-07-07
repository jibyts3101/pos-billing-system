package com.pos.posbillingsystem.product.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pos.posbillingsystem.product.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    Optional<Product> findByBarcode(String barcode);

    boolean existsByBarcode(String barcode);
}