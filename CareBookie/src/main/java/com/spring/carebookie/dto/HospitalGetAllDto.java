package com.spring.carebookie.dto;

import java.util.List;

import com.spring.carebookie.repository.projection.HospitalGetAllProjection;
import com.spring.carebookie.repository.projection.ServiceByHospitalIdProjection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HospitalGetAllDto {

    private HospitalGetAllProjection information;

    private List<String> wards;

    private List<ServiceByHospitalIdProjection> services;

}
