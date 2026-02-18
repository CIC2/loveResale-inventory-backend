package com.resale.resaleinventory.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.resale.resaleinventory.models.Model;
import java.util.List;

@Repository
public interface ModelRepository extends JpaRepository<Model, Integer> {
    @Query("SELECT u.id FROM Unit u WHERE u.modelId = :modelId")
    List<Integer> findUnitIdsByModelId(@Param("modelId") Integer modelId);
}


