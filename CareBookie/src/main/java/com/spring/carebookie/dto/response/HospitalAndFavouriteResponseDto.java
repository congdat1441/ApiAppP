package com.spring.carebookie.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HospitalAndFavouriteResponseDto {

    private HospitalResponseDto hospital;

    private Long hospitalFavouriteId;
}
