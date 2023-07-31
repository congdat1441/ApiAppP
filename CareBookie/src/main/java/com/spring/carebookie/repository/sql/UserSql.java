package com.spring.carebookie.repository.sql;

public class UserSql {

    private UserSql() {
    }

    public static final String GET_ALL_DOCTORS =
            SqlConstant.FILTER_DOCTOR_FROM_USER +
            " SELECT d.id, \n" +
            "        d.address, \n" +
            "        d.birth_day birthDay, \n" +
            "        d.email, \n" +
            "        d.first_name firstName, \n" +
            "        d.last_name lastName, \n" +
            "        d.image_link imageLink, \n" +
            "        d.knowledge, \n" +
            "        d.phone, \n" +
            "        d.user_id userId, \n" +
            "        d.roleName, \n" +
            "        h.hospital_id hospitalId, \n" +
            "        h.hospital_name hospitalName, \n" +
            "        w.ward_name wardName, \n" +
            "        d.information, \n" +
            "        d.image_url imageUrl, \n" +
            "        CASE \n" +
            "           WHEN ds.star is null then 0 \n" +
            "           ELSE ds.star \n" +
            "        END star"+
            " FROM doctor d" +
            " LEFT JOIN hospital h ON d.hospital_id = h.hospital_id" +
            " LEFT JOIN ward w ON d.ward_id = w.id" +
            " LEFT JOIN (\n" +
            "       SELECT doctor_id, AVG(star) star\n" +
            "       FROM rating_doctor\n" +
            "       GROUP BY doctor_id ) as ds on d.user_id = ds.doctor_id" +
            " ORDER BY ds.star DESC \n";

    public static final String SEARCH_DOCTOR_BY_KEY =
            SqlConstant.FILTER_DOCTOR_FROM_USER +
            " SELECT d.id, \n" +
            "        d.address, \n" +
            "        d.birth_day birthDay, \n" +
            "        d.email, \n" +
            "        d.first_name firstName, \n" +
            "        d.last_name lastName, \n" +
            "        d.image_link imageLink, \n" +
            "        d.knowledge, \n" +
            "        d.phone, \n" +
            "        d.user_id userId, \n" +
            "        d.roleName, \n" +
            "        h.hospital_id hospitalId, \n" +
            "        h.hospital_name hospitalName, \n" +
            "        w.ward_name wardName, \n" +
            "        d.information, \n" +
            "        d.image_url imageUrl, \n"+
            "        CASE \n" +
            "           WHEN ds.star is null then 0 \n" +
            "           ELSE ds.star \n" +
            "        END star "+
            " FROM doctor d\n" +
            " LEFT JOIN hospital h ON d.hospital_id = h.hospital_id" +
            " LEFT JOIN ward w ON d.ward_id = w.id " +
            " LEFT JOIN (\n" +
            "       SELECT doctor_id, AVG(star) star \n" +
            "       FROM rating_doctor \n" +
            "       GROUP BY doctor_id ) as ds on d.user_id = ds.doctor_id " +
            " WHERE lower(concat(d.last_name,' ',d.first_name)) like lower(concat('%',:key,'%'))" +
            " ORDER BY ds.star DESC \n";

    public static final String GET_ALL_DOCTOR_BY_HOSPITAL_ID =
            SqlConstant.FILTER_DOCTOR_FROM_USER +
            " SELECT d.id, \n" +
            "        d.address, \n" +
            "        d.birth_day birthDay, \n" +
            "        d.email, \n" +
            "        d.first_name firstName, \n" +
            "        d.last_name lastName, \n" +
            "        d.image_link imageLink, \n" +
            "        d.knowledge, \n" +
            "        d.phone, \n" +
            "        d.user_id userId, \n" +
            "        d.roleName, \n" +
            "        h.hospital_id hospitalId, \n" +
            "        h.hospital_name hospitalName, \n" +
            "        w.ward_name wardName, \n" +
            "        d.information, \n" +
            "        d.image_url imageUrl, \n" +
            "        CASE \n" +
            "           WHEN ds.star is null then 0 \n" +
            "           ELSE ds.star \n" +
            "        END star"+
            " FROM doctor d" +
            " LEFT JOIN hospital h ON d.hospital_id = h.hospital_id" +
            " LEFT JOIN ward w ON d.ward_id = w.id" +
            " LEFT JOIN (\n" +
            "       SELECT doctor_id, AVG(star) star\n" +
            "       FROM rating_doctor\n" +
            "       GROUP BY doctor_id ) as ds on d.user_id = ds.doctor_id" +
            " WHERE d.hospital_id = :hospitalId" +
            " ORDER BY ds.star DESC \n";
}
