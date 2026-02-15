package com.resale.homeflyinventory.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "unit")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Unit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name_en", length = 255)
    private String nameEn;

    @Column(name = "name_ar", length = 255)
    private String nameAr;

    @Column(name = "usage_type_id")
    private Integer usageTypeId;

    @Column(name = "building_id")
    private Integer buildingId;

    @Column(name = "business_entity_id")
    private Integer businessEntityId;

    @Column(name = "project_id")
    private Integer projectId;

    @Column(name = "model_id")
    private Integer modelId;

    @Column(name = "floor_no", length = 50)
    private String floorNo;

    @Column(name = "coordinates_x")
    private Double coordinatesX;

    @Column(name = "coordinates_y")
    private Double coordinatesY;

    @Column(name = "area")
    private Integer area;

    @Column(name = "base_price", length = 255)
    private String basePrice;

    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "current_viewers_count")
    private Integer currentViewersCount;

    @Column(name = "virtual_tour_url", length = 500)
    private String virtualTourUrl;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

       @Column(name = "address")
        private String address;

        @Column(name = "address_ar")
        private String addressAr;

        @Column(name = "balcony")
        private String balcony;

        @Column(name = "bathroom")
        private String bathroom;

        @Column(name = "delivery")
        private String delivery;

        @Column(name = "delivery_date")
        private String deliveryDate;

        @Column(name = "delivery_text_ar")
        private String deliveryTextAr;

        @Column(name = "delivery_text")
        private String deliveryText;

        @Column(name = "down_payment")
        private String downPayment;

        @Column(name = "f_ac")
        private String fAc;

        @Column(name = "f_club")
        private String fClub;

        @Column(name = "f_garage")
        private String fGarage;

        @Column(name = "f_storage")
        private String fStorage;

        @Column(name = "garden")
        private String garden;

        @Column(name = "kitchen")
        private String kitchen;

        @Column(name = "meas_unit")
        private String measUnit;

        @Column(name = "number_of_rooms")
        private String numberOfRooms;

        @Column(name = "old_unit_code")
        private String oldUnitCode;

        @Column(name = "region")
        private String region;

        @Column(name = "rental_unit")
        private String rentalUnit;

        @Column(name = "reservation_amount")
        private String reservationAmount;

        @Column(name = "ru_view")
        private String ruView;

        @Column(name = "ru_view_desc")
        private String ruViewDesc;

        @Column(name = "status_desc")
        private String statusDesc;

        @Column(name = "subregion")
        private String subregion;

        @Column(name = "unit_desc")
        private String unitDesc;

        @Column(name = "unit_model")
        private String unitModel;

        @Column(name = "utility")
        private String utility;

        @Column(name = "number_of_floors")
        private String numberOfFloors;

        @Column(name = "unit_model_code")
        private String unitModelCode;

        @Column(name = "updated_at")
        private LocalDateTime updatedAt;
    }


