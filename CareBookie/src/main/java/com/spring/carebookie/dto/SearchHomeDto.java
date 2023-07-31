package com.spring.carebookie.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchHomeDto {

    private List<HospitalGetAllDto> hospitals;

    private List<DoctorGetAllDto> doctors;

}
