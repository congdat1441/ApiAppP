package com.spring.carebookie.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.spring.carebookie.entity.RatingHospitalEntity;
import com.spring.carebookie.repository.projection.StarProjection;
import com.spring.carebookie.repository.sql.RatingHospitalSql;

public interface RatingHospitalRepository extends JpaRepository<RatingHospitalEntity, Long> {

    @Query(value = RatingHospitalSql.HOSPITAL_STAR,nativeQuery = true)
    List<StarProjection> getHospitalStar();

    List<RatingHospitalEntity> getAllByHospitalIdOrderByDateTime(String hospitalId);
}
