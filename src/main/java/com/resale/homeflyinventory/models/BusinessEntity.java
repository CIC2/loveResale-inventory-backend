package com.resale.homeflyinventory.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "business_entity")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusinessEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "code")
    private String code;

    @Column(name = "project_id")
    private Integer projectId;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}


