package com.spring.carebookie.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spring.carebookie.dto.edit.MedicineUpdateDto;
import com.spring.carebookie.entity.MedicineEntity;

public interface MedicineRepository extends JpaRepository<MedicineEntity,Long> {

    @Query("select m from MedicineEntity m where m.hospitalId = ?1")
    List<MedicineEntity> getAllByHospitalId(String hospitalId);

    @Modifying
    @Query("update MedicineEntity m set m.medicineUnit = :#{#dto.medicineUnit}, m.medicinePrice = :#{#dto.medicinePrice} ," +
            " m.medicineName = :#{#dto.medicineName} where m.id = :#{#dto.id}")
    void update(@Param("dto") MedicineUpdateDto dto);

    @Query("select m from MedicineEntity m where m.hospitalId = :hospitalId and lower(m.medicineName) like concat('%',lower(:name) ,'%') ")
    List<MedicineEntity> getAllByHospitalIdAnAndMedicineName(String hospitalId, String name);

    @Modifying
    @Query("update MedicineEntity m set m.isDisable = true where m.id = :medicineId")
    void lockMedicine(Long medicineId);
}
