package com.pos.posbillingsystem.product.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pos.posbillingsystem.product.dto.ProductRequest;
import com.pos.posbillingsystem.product.dto.ProductResponse;
import com.pos.posbillingsystem.product.entity.Product;
import com.pos.posbillingsystem.product.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    public ProductResponse createProduct(ProductRequest request) {

        if (productRepository.existsByBarcode(request.getBarcode())) {
            throw new RuntimeException("Barcode already exists");
        }

        Product product = new Product();

        product.setBarcode(request.getBarcode());
        product.setProductName(request.getProductName());
        product.setBrand(request.getBrand());
        product.setCategoryId(request.getCategoryId());
        product.setSupplierId(request.getSupplierId());
        product.setPurchasePrice(request.getPurchasePrice());
        product.setSellingPrice(request.getSellingPrice());
        product.setGstPercentage(request.getGstPercentage());
        product.setStockQuantity(request.getStockQuantity());
        product.setMinimumStock(request.getMinimumStock());
        product.setManufactureDate(request.getManufactureDate());
        product.setExpiryDate(request.getExpiryDate());
        product.setProductImage(request.getProductImage());
        product.setStatus(request.getStatus());

        Product savedProduct = productRepository.save(product);

        return mapToResponse(savedProduct);
    }
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    
    public ProductResponse getProductById(Integer id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return mapToResponse(product);
    }
    public ProductResponse updateProduct(Integer id, ProductRequest request) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (!product.getBarcode().equals(request.getBarcode())
                && productRepository.existsByBarcode(request.getBarcode())) {
            throw new RuntimeException("Barcode already exists");
        }

        product.setBarcode(request.getBarcode());
        product.setProductName(request.getProductName());
        product.setBrand(request.getBrand());
        product.setCategoryId(request.getCategoryId());
        product.setSupplierId(request.getSupplierId());
        product.setPurchasePrice(request.getPurchasePrice());
        product.setSellingPrice(request.getSellingPrice());
        product.setGstPercentage(request.getGstPercentage());
        product.setStockQuantity(request.getStockQuantity());
        product.setMinimumStock(request.getMinimumStock());
        product.setManufactureDate(request.getManufactureDate());
        product.setExpiryDate(request.getExpiryDate());
        product.setProductImage(request.getProductImage());
        product.setStatus(request.getStatus());

        Product updatedProduct = productRepository.save(product);

        return mapToResponse(updatedProduct);
    }
    public void deleteProduct(Integer id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        productRepository.delete(product);
    }
    public List<Product> searchByProductName(String productName) {
        return productRepository.findByProductNameContainingIgnoreCase(productName);
    }

    public List<Product> searchByBarcode(String barcode) {
        return productRepository.findByBarcode(barcode);
    }
    private ProductResponse mapToResponse(Product product) {

        ProductResponse response = new ProductResponse();

        response.setProductId(product.getProductId());
        response.setBarcode(product.getBarcode());
        response.setProductName(product.getProductName());
        response.setBrand(product.getBrand());
        response.setCategoryId(product.getCategoryId());
        response.setSupplierId(product.getSupplierId());
        response.setPurchasePrice(product.getPurchasePrice());
        response.setSellingPrice(product.getSellingPrice());
        response.setGstPercentage(product.getGstPercentage());
        response.setStockQuantity(product.getStockQuantity());
        response.setMinimumStock(product.getMinimumStock());
        response.setManufactureDate(product.getManufactureDate());
        response.setExpiryDate(product.getExpiryDate());
        response.setProductImage(product.getProductImage());
        response.setStatus(product.getStatus());
        response.setCreatedAt(product.getCreatedAt());
        response.setUpdatedAt(product.getUpdatedAt());

        return response;
    }

}