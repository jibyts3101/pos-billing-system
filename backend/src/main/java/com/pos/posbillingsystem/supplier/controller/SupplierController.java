package com.pos.posbillingsystem.supplier.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pos.posbillingsystem.supplier.dto.SupplierRequest;
import com.pos.posbillingsystem.supplier.dto.SupplierResponse;
import com.pos.posbillingsystem.supplier.service.SupplierService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    // Create Supplier
    @PostMapping
    public ResponseEntity<SupplierResponse> createSupplier(
            @Valid @RequestBody SupplierRequest request) {

        SupplierResponse response = supplierService.createSupplier(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Get All Suppliers
    @GetMapping
    public ResponseEntity<List<SupplierResponse>> getAllSuppliers() {

        return ResponseEntity.ok(supplierService.getAllSuppliers());
    }

    // Get Supplier By Id
    @GetMapping("/{id}")
    public ResponseEntity<SupplierResponse> getSupplierById(
            @PathVariable Integer id) {

        return ResponseEntity.ok(supplierService.getSupplierById(id));
    }

    // Update Supplier
    @PutMapping("/{id}")
    public ResponseEntity<SupplierResponse> updateSupplier(
            @PathVariable Integer id,
            @Valid @RequestBody SupplierRequest request) {

        return ResponseEntity.ok(
                supplierService.updateSupplier(id, request));
    }

    // Delete Supplier
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSupplier(
            @PathVariable Integer id) {

        supplierService.deleteSupplier(id);

        return ResponseEntity.ok("Supplier deleted successfully");
    }
}