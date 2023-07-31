package com.spring.carebookie.dto.save;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MedicineSaveDto {

    @NotBlank
    private String hospitalId;

    @NotBlank
    private String medicineName;

    private Double medicinePrice;

    @NotBlank
    private String medicineUnit;
}
