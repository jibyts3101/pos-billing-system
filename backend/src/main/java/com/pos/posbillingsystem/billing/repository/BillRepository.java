package com.pos.posbillingsystem.billing.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pos.posbillingsystem.billing.entity.Bill;

public interface BillRepository extends JpaRepository<Bill, Integer> {

    boolean existsByInvoiceNumber(String invoiceNumber);

    Optional<Bill> findByInvoiceNumber(String invoiceNumber);

    List<Bill> findByCustomerId(Integer customerId);

    List<Bill> findByBillDateBetween(LocalDateTime start, LocalDateTime end);
}