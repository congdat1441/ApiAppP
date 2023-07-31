package com.spring.carebookie.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SearchHomeDoctorResponse {

    private String doctorId;

    private String doctorName;

    private String imageUrl;

    private String speciality;
}
