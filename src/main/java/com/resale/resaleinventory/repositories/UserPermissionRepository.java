package com.resale.resaleinventory.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.resale.resaleinventory.models.UserPermission;

import java.util.List;

@Repository
public interface UserPermissionRepository extends JpaRepository<UserPermission, Integer> {
    List<UserPermission> findByUserId(Integer userId);

}


