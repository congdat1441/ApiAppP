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
public class DoctorGetAllDto {

    private String userId;

    private String firstName;

    private String lastName;

    private String birthDay;

    private String email;

    private String phone;

    private String address;

    private String imageLink;

    private List<String> knowledges;

    private String hospitalId;

    private String hospitalName;

    private String wardName;

    private String roleName;

    private String information;

    private String imageUrl;

    private Double star;

}
