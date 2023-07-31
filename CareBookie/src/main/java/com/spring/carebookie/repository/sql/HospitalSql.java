package com.spring.carebookie.repository.sql;

public class HospitalSql {

    private HospitalSql() {
    }
    
    public static final String GET_ALL_HOSPITAL = 
            SqlConstant.HOSPITAL_STAR +
            " SELECT h.hospital_id hospitalId,\n" +
            "        h.hospital_name hospitalName,\n" +
            "        h.phone_number phoneNumber,\n" +
            "        h.address,\n" +
            "        h.district,\n" +
            "        h.price,\n" +
            "        h.logo_key logoKey,\n" +
            "        h.working_time_from workingTimeFrom,\n" +
            "        h.working_time_to workingTimeTo,\n" +
            "        h.period_working_day_from periodWorkingDayFrom,\n" +
            "        h.period_working_day_to periodWorkingDayTo, \n" +
            "        hs.star, \n" +
            "        CONCAT(u.first_name,' ',u.last_name) 'adminName', \n" +
            "        u.user_id adminId, \n " +
            "        h.information, \n" +
            "        h.image_url imageUrl"+
            " FROM hospital h" +
            " LEFT JOIN hospital_star  hs ON h.hospital_id = hs.hospital_id" +
            " LEFT JOIN user u on h.admin_id = u.user_id;";

    public static final String GET_ALL_SERVICE_BY_HOSPITAL_ID =
            " SELECT s.service_name serviceName,\n" +
            "       s.price \n" +
            " FROM hospital h \n" +
            " LEFT JOIN hospital_service hs on h.hospital_id = hs.hospital_id \n" +
            " LEFT JOIN service s on hs.service_id = s.id \n" +
            " WHERE h.hospital_id = :hospitalId";

    public static final String GET_WARD_BY_HOSPITAL_ID =
            " SELECT w.ward_name \n" +
            " FROM hospital h \n" +
            " LEFT JOIN hospital_ward hw ON h.hospital_id = hw.hospital_id \n" +
            " JOIN ward w ON hw.ward_id = w.id \n" +
            "       WHERE h.hospital_id = :hospitalId";

    public static final String SEARCH_HOSPITAL_BY_KEY =
            SqlConstant.HOSPITAL_STAR +
            "SELECT distinct " +
            "       h.hospital_id hospitalId, \n" +
            "       h.hospital_name hospitalName, \n" +
            "       h.phone_number phoneNumber, \n" +
            "       h.address, \n" +
            "       h.district, \n" +
            "       h.price, \n" +
            "       h.logo_key logoKey, \n" +
            "       h.working_time_from workingTimeFrom, \n" +
            "       h.working_time_to workingTimeTo, \n" +
            "       h.period_working_day_from periodWorkingDayFrom, \n" +
            "       h.period_working_day_to periodWorkingDayTo,  \n" +
            "       hs.star,  \n" +
            "       CONCAT(u.first_name,' ',u.last_name) 'adminName', \n" +
            "       u.user_id adminId, \n" +
            "       h.information, \n" +
            "       h.image_url imageUrl"+
            " FROM hospital h \n" +
            " LEFT JOIN hospital_ward hw ON h.hospital_id = hw.hospital_id \n" +
            " LEFT JOIN ward w ON hw.ward_id = w.id \n" +
            " LEFT JOIN user u on h.admin_id = u.user_id \n" +
            " LEFT JOIN hospital_star  hs ON h.hospital_id = hs.hospital_id \n" +
            "       WHERE lower(h.hospital_name) LIKE lower(concat('%',:key,'%')) \n" +
            "           OR lower(w.ward_name) LIKE lower(concat('%',:key,'%'));";
}
