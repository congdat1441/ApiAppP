package com.spring.carebookie.dto.response;

import java.util.List;

import com.spring.carebookie.entity.HospitalEntity;
import com.spring.carebookie.entity.InvoiceEntity;
import com.spring.carebookie.entity.MedicineEntity;
import com.spring.carebookie.entity.ServiceBookEntity;
import com.spring.carebookie.entity.ServiceEntity;
import com.spring.carebookie.entity.UserEntity;
import com.spring.carebookie.repository.projection.InvoiceMedicineAmountProjection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvoiceResponseDto {

    private UserInvoiceResponse user;

    private String hospitalName;

    private String address;

    private Double star;

    private String imageUrl;

    private String doctorName;

    private String operatorName;

    private Double totalPrice;

    private InvoiceEntity invoiceInformation;

    private List<ServiceEntity> services;

    private List<InvoiceMedicineAmountProjection> medicines;

    public InvoiceResponseDto(InvoiceEntity i, List<ServiceEntity> serviceInvoice, List<InvoiceMedicineAmountProjection> medicineInvoice) {
        this.invoiceInformation = i;
        this.services = serviceInvoice;
        this.medicines =  medicineInvoice;
    }

    public InvoiceResponseDto(double v, InvoiceEntity i, List<ServiceEntity> serviceInvoice, List<InvoiceMedicineAmountProjection> medicineInvoice) {
        this.totalPrice = v;
        this.invoiceInformation = i;
        this.services = serviceInvoice;
        this.medicines =  medicineInvoice;
    }
}
