package com.resale.homeflyinventory.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.resale.homeflyinventory.models.InventoryLog;

import java.util.List;

@Repository
public interface InventoryLogRepository extends JpaRepository<InventoryLog, Integer> {
    @Query("""
    SELECT DISTINCT i.modelId
    FROM InventoryLog i
    WHERE i.actionCode = :actionCode
      AND i.identityType = :identityType
      AND i.identityId = :customerId
      AND i.modelId IS NOT NULL
""")
    List<Integer> findDistinctModelIdsByCustomer(
            @Param("actionCode") Integer actionCode,
            @Param("identityType") String identityType,
            @Param("customerId") Integer customerId
    );
}


