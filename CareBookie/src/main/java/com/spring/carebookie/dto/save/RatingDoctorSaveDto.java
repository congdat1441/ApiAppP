package com.spring.carebookie.dto.save;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RatingDoctorSaveDto {

    private String comment;

    private String doctorId;

    private String userId;

    private Double star;

}
