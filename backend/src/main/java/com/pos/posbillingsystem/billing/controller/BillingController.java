package com.pos.posbillingsystem.billing.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.pos.posbillingsystem.billing.dto.BillRequest;
import com.pos.posbillingsystem.billing.dto.BillResponse;
import com.pos.posbillingsystem.billing.service.BillingService;
import java.time.LocalDate;

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
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<BillResponse>> getBillsByCustomer(
            @PathVariable Integer customerId) {

        return ResponseEntity.ok(
                billingService.getBillsByCustomer(customerId));
    }
    @GetMapping("/date")
    public ResponseEntity<List<BillResponse>> getBillsByDate(
            @RequestParam LocalDate date) {

        return ResponseEntity.ok(
                billingService.getBillsByDate(date));
    }
    @GetMapping("/date-range")
    public ResponseEntity<List<BillResponse>> getBillsBetweenDates(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {

        return ResponseEntity.ok(
                billingService.getBillsBetweenDates(startDate, endDate));
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