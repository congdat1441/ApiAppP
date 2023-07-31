package com.spring.carebookie.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.spring.carebookie.entity.UserFavoriteDoctorEntity;

public interface UserFavouriteDoctorRepository extends JpaRepository<UserFavoriteDoctorEntity, Long> {

    @Query("select u.doctorId from UserFavoriteDoctorEntity u where u.userId = ?1")
    List<String> getAllFavouriteDoctorIdByUserId(String userId);

    @Query("select u.id from UserFavoriteDoctorEntity u where u.userId = ?1")
    List<Long> getAllFavouriteIdByUserId(String userId);

    @Query("select (count(u) > 0) from UserFavoriteDoctorEntity u where u.doctorId = ?1 and u.userId = ?2")
    boolean existsByDoctorIdAndUserId(String doctorId, String userId);
}
