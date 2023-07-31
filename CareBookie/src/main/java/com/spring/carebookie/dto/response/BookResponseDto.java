package com.spring.carebookie.dto.response;

import java.util.List;

import com.spring.carebookie.entity.BookEntity;
import com.spring.carebookie.entity.ServiceEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookResponseDto {

    private String hospitalNameH;

    private String addressH;

    private Double starH;

    private String imageUrlH;

    private String operatorId;

    private String fullNameOperator;

    private String userId;

    private String fullName;

    private int gender;

    private Integer age;

    private String address;

    private String doctorName;

    private BookEntity bookInformation;

    private List<ServiceEntity> services;

    private List<InvoiceResponseDto> invoiceShares;

    public BookResponseDto(BookEntity b, List<ServiceEntity> serviceBooks, List<InvoiceResponseDto> invoiceResponseDtos) {
        this.bookInformation = b;
        this.services = serviceBooks;
        this.invoiceShares = invoiceResponseDtos;
    }
}
