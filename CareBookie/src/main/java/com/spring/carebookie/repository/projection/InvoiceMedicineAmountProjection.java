package com.spring.carebookie.repository.projection;

public interface InvoiceMedicineAmountProjection {

    Long getId();

    String getHospitalId();

    String getMedicineName();

    Double getMedicinePrice();

    String getMedicineUnit();

    Long getAmount();

}
