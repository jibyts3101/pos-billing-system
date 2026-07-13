package com.pos.posbillingsystem.billing.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.pos.posbillingsystem.billing.dto.BillRequest;
import com.pos.posbillingsystem.billing.dto.BillResponse;
import com.pos.posbillingsystem.billing.service.BillingService;

@RestController
@RequestMapping("/api/billing")
public class BillingController {

    private final BillingService billingService;

    public BillingController(BillingService billingService) {
        this.billingService = billingService;
    }

    @PostMapping
    public ResponseEntity<BillResponse> createBill(
            @RequestBody BillRequest request) {

        BillResponse response = billingService.createBill(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @GetMapping("/search")
    public ResponseEntity<BillResponse> searchByInvoice(
            @RequestParam String invoice) {

        BillResponse response = billingService.getBillByInvoice(invoice);

        return ResponseEntity.ok(response);
    }
    @GetMapping("/{billId}")
    public ResponseEntity<BillResponse> getBill(
            @PathVariable Integer billId) {

        BillResponse response = billingService.getBillById(billId);

        return ResponseEntity.ok(response);
    }
    @GetMapping
    public ResponseEntity<List<BillResponse>> getAllBills() {

        List<BillResponse> bills = billingService.getAllBills();

        return ResponseEntity.ok(bills);
    }

}