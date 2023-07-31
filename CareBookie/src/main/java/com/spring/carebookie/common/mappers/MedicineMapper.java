package com.spring.carebookie.common.mappers;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.ReportingPolicy.IGNORE;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.spring.carebookie.dto.save.MedicineSaveDto;
import com.spring.carebookie.entity.MedicineEntity;

@Mapper(unmappedTargetPolicy = IGNORE, nullValueCheckStrategy = ALWAYS)
public interface MedicineMapper {
    MedicineMapper INSTANCE = Mappers.getMapper(MedicineMapper.class);

    MedicineEntity convertSaveDtoToEntity(MedicineSaveDto dto);
}
