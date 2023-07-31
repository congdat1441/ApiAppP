package com.spring.carebookie.common.mappers;

import com.spring.carebookie.dto.save.HospitalSaveDto;
import com.spring.carebookie.dto.save.UserSaveDto;
import com.spring.carebookie.entity.HospitalEntity;
import com.spring.carebookie.entity.UserEntity;

public class TestMapper {
    public static void main(String[] args) {
        HospitalMapper mapper = HospitalMapper.INSTANCE;

        HospitalSaveDto dto = new HospitalSaveDto();

        dto.setHospitalName("A47");

        System.out.println(dto);

        HospitalEntity entity = mapper.convertSaveDtoToEntity(dto);
        System.out.println( " " + entity.getHospitalName());


        UserMapper mapper1 = UserMapper.INSTANCE;
        UserSaveDto dto1 = new UserSaveDto();
        dto1.setAddress("Hue");
        dto1.setEmail("poanh1111@gmail.com");
        UserEntity entity1 = mapper1.convertSaveToEntity(dto1);
        System.out.println(entity1.getAddress());
    }
}
