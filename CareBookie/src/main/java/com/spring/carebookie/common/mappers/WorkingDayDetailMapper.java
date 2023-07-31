package com.spring.carebookie.common.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.spring.carebookie.dto.save.WorkingDayDetailDto;
import com.spring.carebookie.entity.WorkingDayDetailsEntity;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WorkingDayDetailMapper {

    WorkingDayDetailMapper INSTANCE = Mappers.getMapper(WorkingDayDetailMapper.class);

    WorkingDayDetailsEntity convertSaveDtoToEntity(WorkingDayDetailDto dto);

    List<WorkingDayDetailsEntity> convertSaveDtosToEntities(List<WorkingDayDetailDto> dtos);
}
