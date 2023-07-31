package com.spring.carebookie.repository.sql;

public class RatingHospitalSql {

    public static final String HOSPITAL_STAR =
            " select hospital_id id, AVG(star) star \n" +
            " from rating_hospital \n" +
            " group by hospital_id;";
}
