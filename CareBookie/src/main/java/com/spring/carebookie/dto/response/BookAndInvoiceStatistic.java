package com.spring.carebookie.dto.response;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookAndInvoiceStatistic {

    private Map<Integer,StatisticResponse> invoices;

    private Map<Integer, StatisticBookResponse> books;

    private Map<String, StatisticDoctorResponse> doctors;
}
