package com.spring.carebookie.repository.projection;

import java.math.BigDecimal;

public interface ServiceByHospitalIdProjection {

    String getServiceName();

    BigDecimal getPrice();

}
