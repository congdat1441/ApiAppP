package com.spring.carebookie.dto.save;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MedicineIntoInvoiceDto {

    private Long medicineId;

    private Long amount;
}
