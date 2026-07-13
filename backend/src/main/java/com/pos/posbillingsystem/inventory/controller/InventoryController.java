package com.pos.posbillingsystem.inventory.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pos.posbillingsystem.inventory.dto.InventoryResponse;
import com.pos.posbillingsystem.inventory.dto.StockAdjustmentRequest;
import com.pos.posbillingsystem.inventory.dto.StockInRequest;
import com.pos.posbillingsystem.inventory.dto.StockOutRequest;
import com.pos.posbillingsystem.inventory.service.InventoryService;
import com.pos.posbillingsystem.product.entity.Product;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping("/stock-in")
    public ResponseEntity<InventoryResponse> stockIn(
            @RequestBody StockInRequest request) {

        System.out.println("===== INSIDE CONTROLLER =====");

        return new ResponseEntity<>(
                inventoryService.stockIn(request),
                HttpStatus.CREATED);
    }

    @PostMapping("/stock-out")
    public ResponseEntity<InventoryResponse> stockOut(
            @RequestBody StockOutRequest request) {

        return ResponseEntity.ok(
                inventoryService.stockOut(request));
    }

    @PostMapping("/adjust")
    public ResponseEntity<InventoryResponse> stockAdjustment(
            @RequestBody StockAdjustmentRequest request) {

        return ResponseEntity.ok(
                inventoryService.stockAdjustment(request));
    }

    @GetMapping("/history/{productId}")
    public ResponseEntity<List<InventoryResponse>> getHistory(
            @PathVariable Integer productId) {

        return ResponseEntity.ok(
                inventoryService.getHistory(productId));
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<Product>> getLowStockProducts() {

        return ResponseEntity.ok(
                inventoryService.getLowStockProducts());
    }
}