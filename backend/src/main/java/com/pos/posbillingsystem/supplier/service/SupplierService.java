package com.pos.posbillingsystem.supplier.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pos.posbillingsystem.supplier.dto.SupplierRequest;
import com.pos.posbillingsystem.supplier.dto.SupplierResponse;
import com.pos.posbillingsystem.supplier.entity.Supplier;
import com.pos.posbillingsystem.supplier.repository.SupplierRepository;

@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    // CREATE
    public SupplierResponse createSupplier(SupplierRequest request) {

        if (supplierRepository.existsBySupplierName(request.getSupplierName())) {
            throw new RuntimeException("Supplier already exists");
        }

        Supplier supplier = new Supplier();

        supplier.setSupplierName(request.getSupplierName());
        supplier.setContactPerson(request.getContactPerson());
        supplier.setPhone(request.getPhone());
        supplier.setEmail(request.getEmail());
        supplier.setAddress(request.getAddress());
        supplier.setGstNumber(request.getGstNumber());
        supplier.setStatus(request.getStatus());

        Supplier savedSupplier = supplierRepository.save(supplier);

        return mapToResponse(savedSupplier);
    }

    // GET ALL
    public List<SupplierResponse> getAllSuppliers() {

        List<Supplier> suppliers = supplierRepository.findAll();

        return suppliers.stream()
                .map(this::mapToResponse)
                .toList();
    }

    // GET BY ID
    public SupplierResponse getSupplierById(Integer id) {

        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        return mapToResponse(supplier);
    }

    // UPDATE
    public SupplierResponse updateSupplier(Integer id, SupplierRequest request) {

        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        supplier.setSupplierName(request.getSupplierName());
        supplier.setContactPerson(request.getContactPerson());
        supplier.setPhone(request.getPhone());
        supplier.setEmail(request.getEmail());
        supplier.setAddress(request.getAddress());
        supplier.setGstNumber(request.getGstNumber());
        supplier.setStatus(request.getStatus());

        Supplier updatedSupplier = supplierRepository.save(supplier);

        return mapToResponse(updatedSupplier);
    }

    // DELETE
    public void deleteSupplier(Integer id) {

        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        supplierRepository.delete(supplier);
    }

    // DTO Mapper
    private SupplierResponse mapToResponse(Supplier supplier) {

        SupplierResponse response = new SupplierResponse();

        response.setSupplierId(supplier.getSupplierId());
        response.setSupplierName(supplier.getSupplierName());
        response.setContactPerson(supplier.getContactPerson());
        response.setPhone(supplier.getPhone());
        response.setEmail(supplier.getEmail());
        response.setAddress(supplier.getAddress());
        response.setGstNumber(supplier.getGstNumber());
        response.setStatus(supplier.getStatus());
        response.setCreatedAt(supplier.getCreatedAt());
        response.setUpdatedAt(supplier.getUpdatedAt());

        return response;
    }
}