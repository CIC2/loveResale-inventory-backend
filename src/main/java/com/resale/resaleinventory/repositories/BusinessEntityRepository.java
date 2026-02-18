package com.resale.resaleinventory.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.resale.resaleinventory.components.unit.dto.BusinessEntityDTO;
import com.resale.resaleinventory.models.BusinessEntity;

import java.util.List;

@Repository
public interface BusinessEntityRepository extends JpaRepository<BusinessEntity, Integer> {
    @Query("SELECT be.code FROM BusinessEntity be WHERE be.projectId = :projectId")
    List<String> findAllCodesByProjectId(@Param("projectId") Integer projectId);


    @Query("""
        SELECT DISTINCT new tmg.vso.components.unit.dto.BusinessEntityDTO(b.id, b.code)
        FROM BusinessEntity b
        WHERE (:projectIds IS NULL OR b.projectId IN :projectIds)
    """)
    List<BusinessEntityDTO> findByProjectIds(@Param("projectIds") List<Integer> projectIds);
}


