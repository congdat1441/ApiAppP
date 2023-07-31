package com.spring.carebookie.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.spring.carebookie.entity.UserCode;

public interface UserCodeRepository extends JpaRepository<UserCode,Long> {

    @Query("select (count(u) > 0) from UserCode u where u.userId = ?1")
    boolean existsUserCodeByUserId(String userId);

    @Modifying
    @Query("update UserCode u set u.code = :code where u.userId = :userId")
    void update(String userId, String code);

    @Query("select u from UserCode u where u.userId = :userId")
    UserCode findByUserId(String userId);
}
