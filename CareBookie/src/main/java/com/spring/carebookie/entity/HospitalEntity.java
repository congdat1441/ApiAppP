package com.spring.carebookie.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "hospital")
public class HospitalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String hospitalId;

    private String hospitalName;

    private String adminId;

    private Boolean isRate;

    private Boolean isPublicPrice;

    private Boolean isChoosenDoctor;

    private String address;

    private String imageKey;

    private BigDecimal priceFrom;

    private BigDecimal priceTo;

    private boolean status;

    private String information;

    private String imageUrl;

    private boolean isDisable = false;

}
