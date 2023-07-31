package com.spring.carebookie.dto.save;

import javax.validation.constraints.NotBlank;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HospitalSaveDto {

    @NotBlank
    private String hospitalName;

    private Boolean isRate;

    private Boolean isPublicPrice;

    private Boolean isChoosenDoctor;

    private String address;

    private BigDecimal priceFrom;

    private BigDecimal priceTo;

    private String information;

    private String imageUrl;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String email;

    @NotBlank
    private String phone;

    @NotBlank
    private String password;
}
