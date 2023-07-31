package com.spring.carebookie.dto.edit;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MedicineUpdateDto {

    private Long  id;

    @NotBlank
    private String medicineName;

    private Double medicinePrice;

    @NotBlank
    private String medicineUnit;
}
