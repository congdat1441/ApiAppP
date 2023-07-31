package com.spring.carebookie.dto.save;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceSaveDto {

    private String hospitalId;

    private String serviceName;

    private BigDecimal price;

}
