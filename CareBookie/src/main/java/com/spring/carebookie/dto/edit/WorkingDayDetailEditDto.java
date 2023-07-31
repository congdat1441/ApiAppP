package com.spring.carebookie.dto.edit;

import lombok.Data;

@Data
public class WorkingDayDetailEditDto {

    private Long id;

    private String startHour;

    private String endHour;
}
