package com.pos.posbillingsystem.product.controller;

import org.springframework.http.HttpStatus;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pos.posbillingsystem.product.dto.ProductRequest;
import com.pos.posbillingsystem.product.dto.ProductResponse;
import com.pos.posbillingsystem.product.service.ProductService;
import com.pos.posbillingsystem.product.entity.Product;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(
            @PathVariable Integer id) {

        return ResponseEntity.ok(productService.getProductById(id));
    }
    @GetMapping("/search/name")
    public ResponseEntity<List<Product>> searchByProductName(
            @RequestParam String name) {

        return ResponseEntity.ok(productService.searchByProductName(name));
    }

    @GetMapping("/search/barcode")
    public ResponseEntity<List<Product>> searchByBarcode(
            @RequestParam String barcode) {

        return ResponseEntity.ok(productService.searchByBarcode(barcode));
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestBody ProductRequest request) {

        ProductResponse response = productService.createProduct(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Integer id,
            @Valid @RequestBody ProductRequest request) {

        ProductResponse response = productService.updateProduct(id, request);

        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Integer id) {

        productService.deleteProduct(id);

        return ResponseEntity.ok("Product deleted successfully");
    }
}