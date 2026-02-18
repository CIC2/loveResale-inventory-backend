package com.resale.resaleinventory.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.resale.resaleinventory.models.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Integer> {

}

