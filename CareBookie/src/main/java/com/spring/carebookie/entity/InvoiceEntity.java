package com.spring.carebookie.entity;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "invoice")
public class InvoiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long bookId;

    private String symptomDetail;

    private String advices;

    private String diagnose;

    private Double discountInsurance;

    private boolean isExamined;

    private LocalDateTime dateTimeInvoice;

    private String doctorId;

    private String userId;

    private String operatorId;

    private String hospitalId;
}
