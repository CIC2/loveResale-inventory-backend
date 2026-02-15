package com.resale.homeflyinventory.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usage_type")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsageType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 50)
    private String code;

    @Column(length = 255)
    private String description;

    @Column(length = 255)
    private String descriptionAr;

    @Column(length = 255)
    private String name;

    @Column(length = 255)
    private String nameAr;
}


