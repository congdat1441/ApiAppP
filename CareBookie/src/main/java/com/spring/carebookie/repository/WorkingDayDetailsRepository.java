package com.spring.carebookie.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.spring.carebookie.entity.WorkingDayDetailsEntity;

public interface WorkingDayDetailsRepository extends JpaRepository<WorkingDayDetailsEntity, Long> {

    List<WorkingDayDetailsEntity> findAllByHospitalId(String hospitalId);

    @Modifying
    @Query(" update WorkingDayDetailsEntity w set w.endHour = :endHour , w.startHour = :startHour " +
            " where w.id = :id and w.hospitalId = :hospitalId")
    void updateWorkingDay(String hospitalId, Long id, String startHour, String endHour);
}
