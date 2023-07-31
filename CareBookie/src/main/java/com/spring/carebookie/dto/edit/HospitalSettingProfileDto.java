package com.spring.carebookie.dto.edit;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HospitalSettingProfileDto {

    private String hospitalId;

    private Boolean isRate;

    private Boolean isPublicPrice;

    private Boolean isChoosenDoctor;

    private String address;

    private BigDecimal priceFrom;

    private BigDecimal priceTo;

    private String information;

    private String imageUrl;
}
