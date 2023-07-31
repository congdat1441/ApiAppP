package com.spring.carebookie.common.mappers;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.ReportingPolicy.IGNORE;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.spring.carebookie.dto.save.BookSaveDto;
import com.spring.carebookie.entity.BookEntity;

@Mapper(unmappedTargetPolicy = IGNORE, nullValueCheckStrategy = ALWAYS)
public interface BookMapper {

    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    BookEntity convertSaveDtoToEntity(BookSaveDto dto);
}
