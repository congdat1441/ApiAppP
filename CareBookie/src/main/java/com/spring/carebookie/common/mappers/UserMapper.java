package com.spring.carebookie.common.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.spring.carebookie.dto.DoctorGetAllDto;
import com.spring.carebookie.dto.response.DoctorInformationResponseDto;
import com.spring.carebookie.dto.response.DoctorResponseDto;
import com.spring.carebookie.dto.response.EmployeeResponseDto;
import com.spring.carebookie.dto.response.LoginResponseDto;
import com.spring.carebookie.dto.save.AdministrativeSaveDto;
import com.spring.carebookie.dto.save.DoctorSaveDto;
import com.spring.carebookie.dto.save.EmployeeSaveDto;
import com.spring.carebookie.dto.save.RegisterDto;
import com.spring.carebookie.dto.save.UserSaveDto;
import com.spring.carebookie.entity.UserEntity;
import com.spring.carebookie.repository.projection.DoctorGetAllProjection;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserEntity convertSaveToEntity(UserSaveDto dto);

    UserEntity convertSaveToEntity(DoctorSaveDto dto);

    UserEntity convertSaveToEntity(AdministrativeSaveDto dto);

    DoctorGetAllDto convertProjectToDto(DoctorGetAllProjection projection);

    DoctorResponseDto convertEntityToDto(UserEntity entity);

    EmployeeResponseDto convertEntityToEDto(UserEntity entity);

    UserEntity convertSaveToEntity(EmployeeSaveDto dto);

    DoctorInformationResponseDto convertDtoToDto(DoctorResponseDto dto);

    UserEntity convertSaveToEntity(RegisterDto dto);

    LoginResponseDto convertEntityToLoginDto(UserEntity entity);

}