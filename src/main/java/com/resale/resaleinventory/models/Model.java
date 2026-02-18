package com.resale.resaleinventory.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "model")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 100)
    private String code;

    @Column(length = 255)
    private String description;

    @Column(name = "description_ar", length = 255)
    private String descriptionAr;

    @Column(length = 100)
    private String name;

    @Column(name = "name_ar", length = 100)
    private String nameAr;

    @Column(name = "finishing_en", length = 100)
    private String finishingEn;

    @Column(name = "finishing_ar", length = 100)
    private String finishingAr;

    @Column(name = "layout_url", length = 255)
    private String layoutUrl;

    @Column(name = "project_id")
    private Integer projectId;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}


