package com.spring.carebookie.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StatisticResponse {

    private Double revenue;

    private Double service;

    private Double medicine;

}
