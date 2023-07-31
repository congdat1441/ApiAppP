package com.spring.carebookie.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeResponseDto {

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

    private String information;

    private Long roleId;

    private boolean isDoctor;

    private String imageUrl;

    private Double star;

    private List<String> knowledges;

    private boolean isDisable;

}
