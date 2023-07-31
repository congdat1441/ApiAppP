package com.spring.carebookie.dto.save;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceSaveDto {

    private Long invoiceId;

    private String diagnose;

    private String symptomDetail;

    private String advices;

    private List<MedicineIntoInvoiceDto> medicines;

    private List<ServiceIntoInvoiceDto> services;

}
