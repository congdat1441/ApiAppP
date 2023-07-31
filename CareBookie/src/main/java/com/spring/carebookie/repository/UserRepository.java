package com.spring.carebookie.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spring.carebookie.dto.edit.AdministratorUpdateDto;
import com.spring.carebookie.dto.edit.DoctorUpdateInformationDto;
import com.spring.carebookie.dto.save.UpdateUserInformationDto;
import com.spring.carebookie.entity.UserEntity;
import com.spring.carebookie.repository.projection.DoctorGetAllProjection;
import com.spring.carebookie.repository.projection.SearchByKeyDoctorProjection;
import com.spring.carebookie.repository.sql.UserSql;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Modifying
    @Query("update UserEntity u set u.firstName = :#{#dto.firstName}," +
            " u.lastName = :#{#dto.lastName}, u.email = :#{#dto.email}, u.phone = :#{#dto.phone}," +
            " u.address = :#{#dto.address}, u.imageUrl = :#{#dto.imageUrl}, u.birthDay = :#{#dto.birthDay}," +
            " u.gender = :#{#dto.gender} where u.userId = :#{#dto.userId}")
    void updateUser(@Param("dto") UpdateUserInformationDto dto);

    @Query(value = "select distinct u.user_id id, concat(u.last_name, ' ' , u.first_name) as name, u.image_url imageUrl, u.speciality as speciality\n" +
            "from user u\n" +
            "where u.is_doctor = true\n" +
            "and (lower(concat(u.last_name,' ',u.first_name)) like lower(concat('%',:key,'%'))\n" +
            "or lower(u.speciality) like lower(concat('%',:key,'%')))", nativeQuery = true)
    List<SearchByKeyDoctorProjection> searchByKey(String key);

    @Query(value = UserSql.GET_ALL_DOCTORS, nativeQuery = true)
    List<DoctorGetAllProjection> getAllDoctors();

    @Query("select u from UserEntity u where u.isDoctor = true and u.hospitalId = :hospitalId")
    List<UserEntity> getAllDoctorByHospitalId(@Param("hospitalId") String hospitalId);

    @Query(value = UserSql.SEARCH_DOCTOR_BY_KEY, nativeQuery = true)
    List<DoctorGetAllProjection> searchDoctorByKey(@Param("key") String key);

    @Query(value = "select u from UserEntity u where u.isDoctor is true and u.hospitalId = :hospitalId")
    List<UserEntity> findAllByHospitalId(@Param("hospitalId") String hospitalId);

    @Query("select u from UserEntity u where u.isDoctor = true")
    List<UserEntity> findAllByDoctorIsTrue();

    @Query("select u from UserEntity u where u.hospitalId is null")
    List<UserEntity> findAllByHospitalIdIsNull();

    @Query("select u from UserEntity u where u.userId = ?1")
    UserEntity findByUserId(String userId);

    @Query(value = "select u from UserEntity u where u.hospitalId = :hospitalId")
    List<UserEntity> findAllEmployeesByHospitalId(String hospitalId);

    @Query("select u from UserEntity u where u.phone = ?1")
    UserEntity findByPhone(String phone);

    /**
     * Doctor
     */
    @Modifying
    @Query(" update UserEntity u set u.birthDay = :#{#dto.birthDay}, u.imageUrl = :#{#dto.imageUrl} ,u.email = :#{#dto.email}, u.phone = :#{#dto.phone},u.address = :#{#dto.address}," +
            " u.knowledge = :#{#dto.knowledge},u.speciality = :#{#dto.speciality},u.startWorkingDate = :#{#dto.startWorkingDate}," +
            " u.information = :#{#dto.information}, u.status = :#{#dto.status} where u.userId = :#{#dto.userId} and u.isDoctor = true")
    void updateDoctor(@Param("dto") DoctorUpdateInformationDto dto);

    /**
     * Administrator
     */
    @Modifying
    @Query(" update UserEntity u set u.birthDay = :#{#dto.birthDay}, u.imageUrl = :#{#dto.imageUrl} ,u.email = :#{#dto.email}, u.phone = :#{#dto.phone},u.address = :#{#dto.address}," +
            " u.knowledge = :#{#dto.knowledge},u.startWorkingDate = :#{#dto.startWorkingDate}," +
            " u.information = :#{#dto.information}, u.status = :#{#dto.status} where u.userId = :#{#dto.userId}")
    void updateAdministrator(@Param("dto") AdministratorUpdateDto dto);

    @Modifying
    @Query(" update UserEntity u set u.status = :status where u.userId = :doctorId ")
    void updateStatus(String doctorId, String status);

    /**
     * Employee
     */
    @Modifying
    @Query("delete from UserEntity u where u.userId = ?1")
    void deleteByUserId(String userId);

    @Modifying
    @Query("update UserEntity u set u.password = :newPassword where u.userId = :userId")
    void updatePassword(String userId, String newPassword);

    @Modifying
    @Query("update UserEntity u set u.isDisable = true where u.userId = :userId")
    void lockUser(String userId);
}
