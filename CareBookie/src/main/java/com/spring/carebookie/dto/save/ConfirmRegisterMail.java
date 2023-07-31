package com.spring.carebookie.dto.save;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmRegisterMail {

    @NotBlank
    private String firstName;

    @NotBlank(message = "Last name not blank")
    private String lastName;

    @NotBlank
    @Email(message = "Must be constraint @")
    private String email;

}
