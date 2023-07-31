package com.spring.carebookie.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StatisticBookResponse {

    private int book;

    private int cancel;

    private double cancelPercent;

    private int confirm;

    private double confirmPercent;
}
