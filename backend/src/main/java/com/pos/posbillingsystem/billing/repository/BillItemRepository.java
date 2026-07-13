package com.pos.posbillingsystem.billing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pos.posbillingsystem.billing.entity.BillItem;

public interface BillItemRepository extends JpaRepository<BillItem, Integer> {

    List<BillItem> findByBillId(Integer billId);

}