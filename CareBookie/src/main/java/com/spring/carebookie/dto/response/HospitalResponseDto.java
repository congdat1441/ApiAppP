package com.spring.carebookie.dto.response;

import java.math.BigDecimal;
import java.util.List;

import com.spring.carebookie.entity.ServiceEntity;
import com.spring.carebookie.entity.UserEntity;
import com.spring.carebookie.entity.WorkingDayDetailsEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HospitalResponseDto {
    private Long id;

    private String hospitalId;

    private String hospitalName;

    private String adminId;

    private UserEntity adminInformation;

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

    private Double star;

    private List<ServiceEntity> services;

    private List<WorkingDayDetailsEntity> workingDayDetails;
}
