package com.spring.carebookie.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.spring.carebookie.entity.RatingDoctorEntity;
import com.spring.carebookie.repository.projection.StarProjection;
import com.spring.carebookie.repository.sql.RatingDoctorSql;

public interface RatingDoctorRepository extends JpaRepository<RatingDoctorEntity,Long> {

    @Query(value = RatingDoctorSql.DOCTOR_STAR, nativeQuery = true)
    List<StarProjection> getDoctorStar();

    List<RatingDoctorEntity> getAllByDoctorIdOrderByDateTime(String doctorId);
}
