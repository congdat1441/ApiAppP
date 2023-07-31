package com.spring.carebookie.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserInvoiceResponse {

    private String userId;

    private String fullName;

    private Integer age;

    private String gender;

    private String fullNameBook;

    private Integer ageBook;

    private String genderBook;
}
