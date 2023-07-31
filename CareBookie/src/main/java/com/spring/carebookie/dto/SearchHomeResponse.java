package com.spring.carebookie.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SearchHomeResponse {

    List<SearchHomeHospitalResponse> hospitals;

    List<SearchHomeDoctorResponse> doctors;
}
