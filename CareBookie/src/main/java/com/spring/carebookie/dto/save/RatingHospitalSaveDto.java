package com.spring.carebookie.dto.save;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RatingHospitalSaveDto {

    private String comment;

    private Double star;

    private String userId;

    private String hospitalId;

}
