package com.spring.carebookie.dto.save;

import javax.validation.constraints.NotBlank;

import com.spring.carebookie.common.constants.EmployeeStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdministrativeSaveDto {

    private String firstName;

    private String lastName;

    private String birthDay;

    @NotBlank
    private String email;

    private int gender;

    @NotBlank
    private String phone;

    private String address;

    @NotBlank
    private String password;

    private String startWorkingDate;

    private String hospitalId;

    private String information;

}
