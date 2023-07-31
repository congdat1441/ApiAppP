package com.spring.carebookie.repository.projection;

import java.math.BigDecimal;

public interface HospitalGetAllProjection {

    String getHospitalId();

    String getHospitalName();

    String getPhoneNumber();

    String getAddress();

    String getDistrict();

    BigDecimal getPrice();

    String getLogoKey();

    String getWorkingTimeFrom();

    String getWorkingTimeTo();

    String getPeriodWorkingDayFrom();

    String getPeriodWorkingDayTo();

    Double getStar();

    String getAdminName();

    String getAdminId();

    String getInformation();

    String getImageUrl();

}
