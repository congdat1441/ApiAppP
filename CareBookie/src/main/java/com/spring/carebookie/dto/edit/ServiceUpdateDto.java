package com.spring.carebookie.dto.edit;

import javax.validation.constraints.NotBlank;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ServiceUpdateDto {

    private Long serviceId;

    @NotBlank
    private String serviceName;

    private BigDecimal price;
}
