package com.spring.carebookie.service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.carebookie.common.mappers.HospitalMapper;
import com.spring.carebookie.dto.HospitalGetAllDto;
import com.spring.carebookie.dto.edit.HospitalSettingProfileDto;
import com.spring.carebookie.dto.response.HospitalResponseDto;
import com.spring.carebookie.dto.save.HospitalSaveDto;
import com.spring.carebookie.dto.save.RegisterHospital;
import com.spring.carebookie.entity.HospitalEntity;
import com.spring.carebookie.entity.UserEntity;
import com.spring.carebookie.entity.WorkingDayDetailsEntity;
import com.spring.carebookie.exception.ResourceNotFoundException;
import com.spring.carebookie.repository.HospitalRepository;
import com.spring.carebookie.repository.MedicineRepository;
import com.spring.carebookie.repository.ServiceRepository;
import com.spring.carebookie.repository.UserRepository;
import com.spring.carebookie.repository.WorkingDayDetailsRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class HospitalService {

    private final PasswordEncoder passwordEncoder;

    private final HospitalRepository hospitalRepository;

    private final CommonService commonService;

    private final UserRepository userRepository;

    private final MedicineRepository medicineRepository;

    private final ServiceRepository serviceRepository;

    private final WorkingDayDetailsRepository workingDayDetailsRepository;

    private static final HospitalMapper HOSPITAL_MAPPER = HospitalMapper.INSTANCE;

    public List<HospitalGetAllDto> getAllHospital() {

        return hospitalRepository.getAllHospital()
                .stream()
                .map(p -> new HospitalGetAllDto(p, hospitalRepository.getWardsByHospitalId(p.getHospitalId()),
                        hospitalRepository.getAllServiceByHospitalId(p.getHospitalId())))
                .collect(Collectors.toList());
    }

    public List<HospitalResponseDto> getAllHospitals() {
        Map<String, Double> stars = commonService.getHospitalStar();
        List<HospitalEntity> entities = hospitalRepository.findAll();
        List<HospitalResponseDto> dtos = HOSPITAL_MAPPER.convertEntitiesToDtos(entities);
        dtos.forEach(dto -> {
            dto.setStar(stars.get(dto.getHospitalId()) == null ? 0 : stars.get(dto.getHospitalId()));
            dto.setServices(commonService.getAllServiceByHospitalId(dto.getHospitalId()));
            dto.setWorkingDayDetails(commonService.getAllWorkingDayDetailByHospitalId(dto.getHospitalId()));
            dto.setAdminInformation(userRepository.findByUserId(dto.getAdminId()));
        });
        dtos.sort(Comparator.nullsLast(Comparator.comparing(HospitalResponseDto::getStar,
                Comparator.nullsFirst(Double::compareTo)).reversed()));
        return dtos;
    }

    public List<HospitalResponseDto> getHospitalByDistrict(String district) {
        return getAllHospitals()
                .stream()
                .filter(h -> h.getAddress().toLowerCase().contains(district.toLowerCase()))
                .collect(Collectors.toList());
    }

    public HospitalResponseDto getHospitalByHospitalId(String hospitalId) {
        return getAllHospitals().stream()
                .filter(dto -> dto.getHospitalId().equals(hospitalId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Hospital not found"));
    }

    @Transactional
    public HospitalResponseDto saveHospital(HospitalSaveDto dto) {
        if (dto.getAddress() == null) {
            dto.setAddress("");
        }

        HospitalEntity entity = HOSPITAL_MAPPER.convertSaveDtoToEntity(dto);

        entity.setHospitalId(generateHospitalId(entity.getHospitalName()));
        entity.setStatus(true); // Accept working

        // Create an account for this hospital
        UserEntity admin = new UserEntity();
        admin.setImageUrl("https://upload.wikimedia.org/wikipedia/commons/thumb/9/98/OOjs_UI_icon_userAvatar.svg/1200px-OOjs_UI_icon_userAvatar.svg.png?fbclid=IwAR2feu8hZAfDllAJfvFKc4P6lQH3eSJ5Q_lEYm1iz6pDwmez4bSiBZdDhbA");
        admin.setFirstName(dto.getFirstName());
        admin.setLastName(dto.getLastName());
        admin.setUserId(generateUserId(dto.getFirstName(), dto.getLastName(), dto.getEmail()));
        admin.setRoleId(2L);
        admin.setEmail(dto.getEmail());
        admin.setPhone(dto.getPhone());
        admin.setPassword(passwordEncoder.encode(dto.getPassword()));
        admin.setHospitalId(entity.getHospitalId());
        UserEntity adminSave = userRepository.save(admin);

        log.info("Finished save {} hospital into database", entity.getHospitalName());
        entity.setAdminId(adminSave.getUserId());

        HospitalEntity hospital = hospitalRepository.save(entity);
        hospital.setStatus(false);
        hospital.setIsRate(false);
        hospital.setIsChoosenDoctor(false);
        hospital.setIsPublicPrice(false);
        int k = 0;
        // Create working day detail
        for (int i = 1; i < 22; i++) {

            String date = "";
            if (i <= 3) {
                date = "2";
            } else if (i <= 6) {
                date = "3";
            } else if (i <= 9) {
                date = "4";
            } else if (i <= 12) {
                date = "5";
            } else if (i <= 15) {
                date = "6";
            } else if (i <= 18) {
                date = "7";
            } else {
                date = "8";
            }
            String[] sessions = {"Sáng", "Chiều", "Tối"};
            String session = sessions[k];
            k++;
            k = k == 3 ? 0 : k;
            workingDayDetailsRepository.save(new WorkingDayDetailsEntity(null, date, session, null, null, hospital.getHospitalId()));
        }
        return getHospitalByHospitalId(hospital.getHospitalId());
    }

    @Transactional
    public HospitalResponseDto acceptHospital(String hospitalId, boolean accept) {
        hospitalRepository.acceptHospital(hospitalId,accept);
        return getHospitalByHospitalId(hospitalId);
    }

    @Transactional
    public HospitalResponseDto settingProfile(HospitalSettingProfileDto dto) {
        hospitalRepository.settingProfile(dto);
        return getHospitalByHospitalId(dto.getHospitalId());
    }

    private String generateHospitalId(String hospitalName) {
        String[] arr = hospitalName.split(" ");
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for (String a : arr) {
            builder.append(a.toCharArray()[0]);
        }
        System.out.println(builder.append(random.nextInt(10)).append(random.nextInt(10))
                .append(random.nextInt(10)).append(random.nextInt(10)));
        return builder.toString();
    }

    /**
     * Generate userId <br>
     * Example: firstName = "Oanh", lastName = "Pham Van", email = "poanh1002@gmail.com" <br>
     * Result is "OPpoanh1002"
     *
     * @param firstName
     * @param lastName
     * @param email
     * @return String
     */
    private String generateUserId(String firstName, String lastName, String email) {
        return firstName.toCharArray()[0] + String.valueOf(lastName.toCharArray()[0]) + email.split("@")[0];

    }

    public HospitalResponseDto registerHospital(RegisterHospital model) {

        HospitalEntity hospital = new HospitalEntity();
        hospital.setHospitalName(model.getHospitalName());
        hospital.setHospitalId(generateHospitalId(model.getHospitalName()));
        hospital.setStatus(false);
        hospital.setIsRate(false);
        hospital.setIsChoosenDoctor(false);
        hospital.setIsPublicPrice(false);
        // User
        UserEntity user = new UserEntity();
        user.setUserId(generateUserId(model.getFirstName(), model.getLastName(), model.getEmail()));
        user.setFirstName(model.getFirstName());
        user.setLastName(model.getLastName());
        user.setEmail(model.getEmail());
        user.setPhone(model.getPhone());
        user.setPassword(passwordEncoder.encode(model.getPassword()));
        user.setRoleId(2L);
        user.setHospitalId(hospital.getHospitalId());
        user.setImageUrl("https://upload.wikimedia.org/wikipedia/commons/thumb/9/98/OOjs_UI_icon_userAvatar.svg/1200px-OOjs_UI_icon_userAvatar.svg.png?fbclid=IwAR2feu8hZAfDllAJfvFKc4P6lQH3eSJ5Q_lEYm1iz6pDwmez4bSiBZdDhbA");
        userRepository.save(user);

        hospital.setAdminId(user.getUserId());
        hospitalRepository.save(hospital);

        int k = 0;
        // Create working day detail
        for (int i = 1; i < 22; i++) {

            String date = "";
            if (i <= 3) {
                date = "2";
            } else if (i <= 6) {
                date = "3";
            } else if (i <= 9) {
                date = "4";
            } else if (i <= 12) {
                date = "5";
            } else if (i <= 15) {
                date = "6";
            } else if (i <= 18) {
                date = "7";
            } else {
                date = "8";
            }
            String[] sessions = {"Sáng", "Chiều", "Tối"};
            String session = sessions[k];
            k++;
            k = k == 3 ? 0 : k;
            workingDayDetailsRepository.save(new WorkingDayDetailsEntity(null, date, session, null, null, hospital.getHospitalId()));
        }
        return getHospitalByHospitalId(hospital.getHospitalId());
    }

    @Transactional
    public void deleteHospital(String hospitalId) {
        // Delete hospital
        hospitalRepository.delete(hospitalRepository.getHospitalId(hospitalId));

        // Delete employee
        userRepository.deleteAll(userRepository.findAllEmployeesByHospitalId(hospitalId));

        // Delete all working day detail
        workingDayDetailsRepository.deleteAll(workingDayDetailsRepository.findAllByHospitalId(hospitalId));

        // Delete all medicine
        medicineRepository.deleteAll(medicineRepository.getAllByHospitalId(hospitalId));

        // Delete all service
        serviceRepository.deleteAll(serviceRepository.getServiceEntityByHospitalId(hospitalId));
    }

    @Transactional
    public void lockHospital(String hospitalId) {
        hospitalRepository.lockHospital(hospitalId);
    }
}
