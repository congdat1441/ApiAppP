package com.spring.carebookie.dto.edit;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdministratorUpdateDto {

    private String userId;

    @NotBlank
    private String email;

    @NotBlank
    private String phone;

    private String address;

    private String knowledge;

    private String startWorkingDate;

    private String information;

    private String status;

    private String imageUrl;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private String birthDay;
}
