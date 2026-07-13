package com.pos.posbillingsystem.billing.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pos.posbillingsystem.billing.entity.Bill;

public interface BillRepository extends JpaRepository<Bill, Integer> {

    boolean existsByInvoiceNumber(String invoiceNumber);

    Optional<Bill> findByInvoiceNumber(String invoiceNumber);

}