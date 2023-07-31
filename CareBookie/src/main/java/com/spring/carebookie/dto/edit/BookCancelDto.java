package com.spring.carebookie.dto.edit;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookCancelDto {

    private Long bookId;

    private String message;

    @NotBlank
    private String operatorId;

}
