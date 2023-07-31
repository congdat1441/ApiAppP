package com.spring.carebookie.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.carebookie.common.mappers.MedicineMapper;
import com.spring.carebookie.dto.edit.MedicineUpdateDto;
import com.spring.carebookie.dto.save.MedicineSaveDto;
import com.spring.carebookie.entity.MedicineEntity;
import com.spring.carebookie.exception.ResourceNotFoundException;
import com.spring.carebookie.repository.MedicineRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MedicineService {

    private final MedicineRepository medicineRepository;

    private static final MedicineMapper MEDICINE_MAPPER = MedicineMapper.INSTANCE;

    @Transactional
    public MedicineEntity save(MedicineSaveDto dto) {
        return medicineRepository.save(MEDICINE_MAPPER.convertSaveDtoToEntity(dto));
    }

    public List<MedicineEntity> getAllMedicineByHospitalId(String hospitalId) {
        return medicineRepository.getAllByHospitalId(hospitalId);
    }

    @Transactional
    public void delete(Long id) {
        medicineRepository.deleteById(id);
    }

    @Transactional
    public MedicineEntity update(MedicineUpdateDto dto) {
        medicineRepository.update(dto);
        return medicineRepository.findById(dto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Medicine {} not found".replace("{}", dto.getId().toString())));
    }

    public List<MedicineEntity> search(String hospitalId, String name) {
        return medicineRepository.getAllByHospitalIdAnAndMedicineName(hospitalId, name);
    }

    @Transactional
    public void lockMedicine(Long medicineId) {
        medicineRepository.lockMedicine(medicineId);
    }
}
