package com.resale.resaleinventory.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer actionCode;
    private String actionName;
    private String identityType;
    private Integer identityId;
    private String httpMethod;
    private Integer statusCode;

    private Integer modelId;
    private Integer unitId;
    private Integer projectId;
    private Integer locationId;

    @Column(columnDefinition = "TEXT")
    private String headers;

    @Column(columnDefinition = "TEXT")
    private String queryParams;

    @Column(columnDefinition = "TEXT")
    private String requestBody;

    @Column(columnDefinition = "TEXT")
    private String responseBody;

    private Long executionTimeMs;

    private LocalDateTime createdAt = LocalDateTime.now();
}


