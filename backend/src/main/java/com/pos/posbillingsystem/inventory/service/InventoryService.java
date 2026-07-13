package com.pos.posbillingsystem.inventory.service;
import java.util.*;
import com.pos.posbillingsystem.product.entity.Product;
import com.pos.posbillingsystem.product.repository.ProductRepository;

import com.pos.posbillingsystem.inventory.entity.InventoryLog;
import com.pos.posbillingsystem.inventory.enums.InventoryAction;
import com.pos.posbillingsystem.inventory.repository.InventoryLogRepository;

import com.pos.posbillingsystem.inventory.dto.InventoryResponse;
import com.pos.posbillingsystem.inventory.dto.StockAdjustmentRequest;
import com.pos.posbillingsystem.inventory.dto.StockInRequest;
import com.pos.posbillingsystem.inventory.dto.StockOutRequest;

import org.springframework.stereotype.Service;

@Service
public class InventoryService {

    private final ProductRepository productRepository;
    private final InventoryLogRepository inventoryLogRepository;

    public InventoryService(ProductRepository productRepository,
                            InventoryLogRepository inventoryLogRepository) {

        this.productRepository = productRepository;
        this.inventoryLogRepository = inventoryLogRepository;
    }
    public InventoryResponse stockIn(StockInRequest request) {

        System.out.println("===== INSIDE STOCK IN =====");

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setStockQuantity(
                product.getStockQuantity() + request.getQuantity());

        productRepository.save(product);

        InventoryLog log = createInventoryLog(
                product.getProductId(),
                request.getQuantity(),
                InventoryAction.PURCHASE,
                request.getRemarks());

        return mapToResponse(log);
    }
    public InventoryResponse stockOut(StockOutRequest request) {

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getStockQuantity() < request.getQuantity()) {
            throw new RuntimeException("Insufficient stock available");
        }

        product.setStockQuantity(
                product.getStockQuantity() - request.getQuantity());

        productRepository.save(product);

        InventoryLog log = createInventoryLog(
                product.getProductId(),
                -request.getQuantity(),
                InventoryAction.SALE,
                request.getRemarks());

        return mapToResponse(log);
    }
    public InventoryResponse stockAdjustment(StockAdjustmentRequest request) {

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        int currentStock = product.getStockQuantity();
        int adjustedStock = request.getAdjustedStock();

        int difference = adjustedStock - currentStock;

        product.setStockQuantity(adjustedStock);

        productRepository.save(product);

        InventoryLog log = createInventoryLog(
                product.getProductId(),
                difference,
                InventoryAction.ADJUSTMENT,
                request.getRemarks());

        return mapToResponse(log);
    }
    public List<InventoryResponse> getHistory(Integer productId) {

        List<InventoryLog> logs = inventoryLogRepository.findByProductId(productId);

        return logs.stream()
                .map(this::mapToResponse)
                .toList();
    }
    public List<Product> getLowStockProducts() {

        return productRepository.findAll()
                .stream()
                .filter(product ->
                        product.getStockQuantity() <= product.getMinimumStock())
                .toList();
    }
    private InventoryLog createInventoryLog(
            Integer productId,
            Integer quantity,
            InventoryAction action,
            String remarks) {

        InventoryLog log = new InventoryLog();

        log.setProductId(productId);
        log.setQuantityChanged(quantity);
        log.setAction(action);
        log.setRemarks(remarks);

        return inventoryLogRepository.save(log);
    }
    private InventoryResponse mapToResponse(InventoryLog log) {

        InventoryResponse response = new InventoryResponse();

        response.setLogId(log.getLogId());
        response.setProductId(log.getProductId());
        response.setUserId(log.getUserId());
        response.setQuantityChanged(log.getQuantityChanged());
        response.setAction(log.getAction());
        response.setReferenceId(log.getReferenceId());
        response.setReferenceType(log.getReferenceType());
        response.setRemarks(log.getRemarks());
        response.setCreatedAt(log.getCreatedAt());

        return response;
    }

}