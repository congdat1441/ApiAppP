package com.spring.carebookie.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spring.carebookie.dto.edit.HospitalSettingProfileDto;
import com.spring.carebookie.entity.HospitalEntity;
import com.spring.carebookie.repository.projection.HospitalGetAllProjection;
import com.spring.carebookie.repository.projection.SearchByKeyHospitalProjection;
import com.spring.carebookie.repository.projection.ServiceByHospitalIdProjection;
import com.spring.carebookie.repository.sql.HospitalSql;

public interface HospitalRepository extends JpaRepository<HospitalEntity, Long> {

    @Query(value = "select distinct h.hospital_id id, h.hospital_name name, h.image_url imageUrl, h.address as address\n" +
            "from hospital h \n" +
            "join service s on h.hospital_id = s.hospital_id \n" +
            "where lower(h.hospital_name) like lower(concat('%',:key,'%')) \n" +
            "or lower(s.service_name) like lower(concat('%',:key,'%'));", nativeQuery = true)
    List<SearchByKeyHospitalProjection> searchByKey(String key);

    @Query(value = HospitalSql.GET_ALL_HOSPITAL, nativeQuery = true)
    List<HospitalGetAllProjection> getAllHospital();

    @Query(value = HospitalSql.GET_WARD_BY_HOSPITAL_ID, nativeQuery = true)
    List<String> getWardsByHospitalId(@Param("hospitalId") String hospitalId);

    @Query(value = HospitalSql.SEARCH_HOSPITAL_BY_KEY, nativeQuery = true)
    List<HospitalGetAllProjection> searchHospitalByKey(@Param("key") String key);

    @Query(value = HospitalSql.GET_ALL_SERVICE_BY_HOSPITAL_ID, nativeQuery = true)
    List<ServiceByHospitalIdProjection> getAllServiceByHospitalId(@Param("hospitalId") String hospitalId);

    @Modifying
    @Query(" update HospitalEntity h set h.status = :accept where h.hospitalId = :hospitalId ")
    void acceptHospital(String hospitalId, boolean accept);

    @Query("select h from HospitalEntity h where h.hospitalId in (:hospitalIds)")
    List<HospitalEntity> getAllByHospitalId(List<String> hospitalIds);

    @Query("select h from HospitalEntity h where h.hospitalId = :hospitalId")
    HospitalEntity getHospitalId(String hospitalId);

    @Modifying
    @Query("update HospitalEntity h set h.information = :#{#dto.information}," +
            " h.address = :#{#dto.address} , h.priceTo = :#{#dto.priceTo}," +
            " h.priceFrom = :#{#dto.priceFrom} , h.isPublicPrice = :#{#dto.isPublicPrice} ," +
            " h.isChoosenDoctor = :#{#dto.getIsChoosenDoctor} , h.isRate = :#{#dto.getIsRate()}," +
            " h.imageUrl = :#{#dto.imageUrl} where h.hospitalId = :#{#dto.hospitalId}")
    void settingProfile(@Param("dto") HospitalSettingProfileDto dto);

    @Modifying
    @Query(" update HospitalEntity h set h.isDisable = true where h.hospitalId = :hospitalId ")
    void lockHospital(String hospitalId);
}
