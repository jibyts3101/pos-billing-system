package com.pos.posbillingsystem.product.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pos.posbillingsystem.product.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    boolean existsByBarcode(String barcode);

    List<Product> findByProductNameContainingIgnoreCase(String productName);

    List<Product> findByBarcode(String barcode);

    // Used in Inventory Module (Low Stock Report)
    List<Product> findByStockQuantityLessThanEqual(Integer stockQuantity);
}