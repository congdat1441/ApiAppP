package com.spring.carebookie.dto.edit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmInvoiceDto {

    private Long invoiceId;
    private Double discountInsurance;
}
