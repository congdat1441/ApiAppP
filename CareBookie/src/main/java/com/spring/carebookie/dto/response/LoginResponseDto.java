package com.spring.carebookie.dto.response;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import java.util.List;

import com.spring.carebookie.entity.HospitalEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDto {

    private Long id;

    private String userId;

    private String firstName;

    private String lastName;

    private String birthDay;

    private String email;

    private int gender;

    private String phone;

    private String address;

    private String imageKey;

    private String password;

    private List<String> knowledges;

    private String speciality;

    private String startWorkingDate;

    private String status;

    private boolean isDoctor;

    private boolean isDisable;

    private String hospitalId;

    private Long roleId;

    private String information;

    private String imageUrl;

    private Double star;

    private HospitalEntity hospital;
}
