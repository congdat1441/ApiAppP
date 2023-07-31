package com.spring.carebookie.dto.response;

import java.util.List;

import lombok.Data;

@Data
public class DoctorInformationResponseDto {
    private Long id;

    private String userId;

    private String firstName;

    private String lastName;

    private String birthDay;

    private String email;

    private String gender;

    private String phone;

    private String address;

    private String imageKey;

    private String speciality;

    private String startWorkingDate;

    private String status;

    private String hospitalId;

    private String hospitalName;

    private String hospitalAddress;

    private String information;

    private String imageUrl;

    private Double star;

    private List<String> knowledges;
}
