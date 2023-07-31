package com.spring.carebookie.repository.sql;

public class RatingDoctorSql {

    public static final String DOCTOR_STAR =
            " select doctor_id  id, AVG(star) star \n" +
            " from rating_doctor \n" +
            " group by doctor_id;";
}
