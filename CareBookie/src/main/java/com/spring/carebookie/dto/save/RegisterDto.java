package com.spring.carebookie.dto.save;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {

    @NotBlank
    private String firstName;

    @NotBlank(message = "Last name not blank")
    private String lastName;

    @NotBlank
    @Email(message = "Must be constraint @")
    private String email;

    @NotBlank
    @Length(min = 10,message = "Phone must have length 10")
    private String phone;

    @NotBlank
    private String password;

}
