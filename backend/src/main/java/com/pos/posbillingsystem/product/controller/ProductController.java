package com.pos.posbillingsystem.product.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pos.posbillingsystem.product.dto.ProductRequest;
import com.pos.posbillingsystem.product.dto.ProductResponse;
import com.pos.posbillingsystem.product.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestBody ProductRequest request) {

        ProductResponse response = productService.createProduct(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}