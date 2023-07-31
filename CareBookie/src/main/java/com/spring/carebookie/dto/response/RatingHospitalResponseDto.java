package com.spring.carebookie.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.spring.carebookie.entity.RatingHospitalEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RatingHospitalResponseDto {

        private Long id;

        private String comment;

        private String hospitalId;

        private String userId;

        private String fullName;

        private String imageUrl;

        private Double star;

        private LocalDateTime dateTime ;

}
