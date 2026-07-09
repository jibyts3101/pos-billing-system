package com.pos.posbillingsystem.category.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pos.posbillingsystem.category.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Optional<Category> findByCategoryName(String categoryName);

    boolean existsByCategoryName(String categoryName);
}