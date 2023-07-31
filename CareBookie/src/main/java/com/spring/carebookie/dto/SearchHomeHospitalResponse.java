package com.spring.carebookie.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SearchHomeHospitalResponse {

    private String hospitalId;

    private String hospitalName;

    private String imageUrl;

    private Double star;

    private String address;

}
