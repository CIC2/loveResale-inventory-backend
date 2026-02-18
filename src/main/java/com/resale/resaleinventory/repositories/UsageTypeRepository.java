package com.resale.resaleinventory.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.resale.resaleinventory.components.unit.dto.UsageTypeDTO;
import com.resale.resaleinventory.models.UsageType;

import java.util.List;

@Repository
public interface UsageTypeRepository extends JpaRepository<UsageType, Integer> {


    @Query("""
        SELECT DISTINCT new tmg.vso.components.unit.dto.UsageTypeDTO(uT.id, uT.name, uT.nameAr)
        FROM Unit u
        JOIN UsageType uT ON u.usageTypeId = uT.id
        WHERE u.projectId IN :projectIds AND u.status = "AV_N"
    """)
    List<UsageTypeDTO> findUnitTypeByProjects(@Param("projectIds") List<Integer> projectIds);
}

