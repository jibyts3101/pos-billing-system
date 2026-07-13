package com.pos.posbillingsystem.inventory.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pos.posbillingsystem.inventory.entity.InventoryLog;

@Repository
public interface InventoryLogRepository extends JpaRepository<InventoryLog, Integer> {

    List<InventoryLog> findByProductId(Integer productId);

}