package com.spring.carebookie.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.spring.carebookie.entity.ServiceEntity;

public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {

    List<ServiceEntity> getServiceEntityByHospitalId(String hospitalId);

    @Modifying
    @Query("update ServiceEntity s set s.serviceName = :name, s.price = :price where s.id = :serviceId")
    void updateService(Long serviceId, String name, BigDecimal price);

    @Query("select s from ServiceEntity  s where s.hospitalId =:hospitalId and lower(s.serviceName) like concat('%',lower(:name) ,'%')  ")
    List<ServiceEntity> search(String hospitalId, String name);

    @Modifying
    @Query("update ServiceEntity s set s.isDisable = true where s.id = :serviceId")
    void lockService(Long serviceId);
}
