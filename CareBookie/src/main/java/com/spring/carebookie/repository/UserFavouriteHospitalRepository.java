package com.spring.carebookie.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.spring.carebookie.entity.UserFavoriteHospitalEntity;

public interface UserFavouriteHospitalRepository extends JpaRepository<UserFavoriteHospitalEntity, Long> {

    @Query("select u.hospitalId from UserFavoriteHospitalEntity u where u.userId = ?1")
    List<String> getAllFavouriteHospitalIdByUserId(String userId);

    @Query("select (count(u) > 0) from UserFavoriteHospitalEntity u where u.hospitalId = ?1 and u.userId = ?2")
    boolean existsByHospitalIdAndUserId(String hospitalId, String userId);

    @Query("select u.id from UserFavoriteHospitalEntity u where u.userId = ?1")
    List<Long> getAllFavouriteIdByUserId(String userId);
}
