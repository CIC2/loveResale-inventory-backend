package com.resale.homeflyinventory.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.resale.homeflyinventory.models.InventoryExceptionLog;

@Repository
public interface InventoryExceptionLogRepository extends JpaRepository<InventoryExceptionLog, Integer> {
}

