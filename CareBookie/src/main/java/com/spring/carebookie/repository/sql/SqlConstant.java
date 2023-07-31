package com.spring.carebookie.repository.sql;

public class SqlConstant {

    private SqlConstant() {
    }

    public static final String FILTER_DOCTOR_FROM_USER =
            " WITH doctor AS (\n" +
            "    SELECT u.*,\n" +
            "           r.name as 'roleName'\t\n" +
            "    FROM user u LEFT JOIN role r on u.role_id = r.id\n" +
            "    WHERE hospital_id is not null and r.name = 'DOCTOR' and u.is_disable = false \n" +
            ") \n";

    public static final String HOSPITAL_STAR =
            " WITH hospital_star AS (" +
            "       SELECT hospital_id, AVG(star) star" +
            "       FROM rating_hospital" +
            "       GROUP BY hospital_id" +
            " ) \n";

}
