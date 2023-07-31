package com.spring.carebookie.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.carebookie.common.mappers.UserMapper;
import com.spring.carebookie.dto.DoctorGetAllDto;
import com.spring.carebookie.dto.EmailDetails;
import com.spring.carebookie.dto.ForgotPasswordDto;
import com.spring.carebookie.dto.LoginRequest;
import com.spring.carebookie.dto.edit.AdministratorUpdateDto;
import com.spring.carebookie.dto.edit.ChangePasswordDto;
import com.spring.carebookie.dto.edit.DoctorUpdateInformationDto;
import com.spring.carebookie.dto.response.DoctorInformationResponseDto;
import com.spring.carebookie.dto.response.DoctorResponseDto;
import com.spring.carebookie.dto.response.EmployeeResponseDto;
import com.spring.carebookie.dto.response.HospitalResponseDto;
import com.spring.carebookie.dto.response.LoginResponseDto;
import com.spring.carebookie.dto.save.AdministrativeSaveDto;
import com.spring.carebookie.dto.save.CheckConfirmRegisterMail;
import com.spring.carebookie.dto.save.ConfirmRegisterMail;
import com.spring.carebookie.dto.save.DoctorSaveDto;
import com.spring.carebookie.dto.save.EmployeeSaveDto;
import com.spring.carebookie.dto.save.RegisterDto;
import com.spring.carebookie.dto.save.UpdateUserInformationDto;
import com.spring.carebookie.dto.save.UserSaveDto;
import com.spring.carebookie.entity.UserCode;
import com.spring.carebookie.entity.UserEntity;
import com.spring.carebookie.exception.ResourceNotFoundException;
import com.spring.carebookie.repository.HospitalRepository;
import com.spring.carebookie.repository.UserCodeRepository;
import com.spring.carebookie.repository.UserRepository;
import com.spring.carebookie.repository.projection.DoctorGetAllProjection;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {

    private final UserCodeRepository userCodeRepository;

    private final UserCodeService userCodeService;

    private final EmailService emailService;

    private final HospitalService hospitalService;

    private final HospitalRepository hospitalRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final CommonService commonService;

    private static final UserMapper USER_MAPPER = UserMapper.INSTANCE;

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public List<UserEntity> getAllPatients() {
        return userRepository.findAllByHospitalIdIsNull();
    }

    public UserEntity getUserByUserId(String userId) {
        return userRepository.findByUserId(userId);
    }

    /**
     * User
     */

    public UserCode confirmRegisterMail(ConfirmRegisterMail dto) {
        Random random = new Random();
        String id = String.format("%04d", random.nextInt(10000));
        String userId = generateUserId(dto.getFirstName(), dto.getLastName(), dto.getEmail());
        emailService.sendSimpleMail(new EmailDetails(dto.getEmail(), id, "Confirm email", null));
        return userCodeService.upsert(userId, id);
    }

    public boolean checkCodeConfirmMail(CheckConfirmRegisterMail dto) {
        String userId = generateUserId(dto.getFirstName(), dto.getLastName(), dto.getEmail());
        UserCode userCode = userCodeRepository.findByUserId(userId);
        return userCode.getCode().equals(dto.getCode());
    }

    @Transactional
    public UserEntity register(RegisterDto dto) {
        // check mail and phone existed
        UserEntity entity = USER_MAPPER.convertSaveToEntity(dto);
        String userId = generateUserId(entity.getFirstName(), entity.getLastName(), entity.getEmail());
        entity.setUserId(userId);
        entity.setRoleId(5L);
        entity.setDisable(false);
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        entity.setImageUrl("https://upload.wikimedia.org/wikipedia/commons/thumb/9/98/OOjs_UI_icon_userAvatar.svg/1200px-OOjs_UI_icon_userAvatar.svg.png?fbclid=IwAR2feu8hZAfDllAJfvFKc4P6lQH3eSJ5Q_lEYm1iz6pDwmez4bSiBZdDhbA");
        UserEntity user = userRepository.save(entity);
        return user;
    }

    @Transactional
    public UserEntity updateUser(UpdateUserInformationDto dto) {
        userRepository.updateUser(dto);
        return Optional.of(userRepository.findByUserId(dto.getUserId()))
                .orElseThrow(() -> new ResourceNotFoundException("User {} not found".replace("{}", dto.getUserId())));
    }

    @Transactional
    public UserEntity save(UserSaveDto dto) {

        UserEntity entity = USER_MAPPER.convertSaveToEntity(dto);

        // Set some information into entity
        String userId = generateUserId(entity.getFirstName(), entity.getLastName(), entity.getEmail());
        entity.setUserId(userId);
        entity.setRoleId(5L);
        entity.setDisable(false);
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));

        return userRepository.save(entity);

    }

    /**
     * Doctor
     */
    @Transactional
    public UserEntity saveDoctor(DoctorSaveDto dto) {

        UserEntity entity = USER_MAPPER.convertSaveToEntity(dto);
        entity.setDisable(false);
        entity.setRoleId(4L);
        entity.setDoctor(true);
        entity.setUserId(generateUserId(entity.getFirstName(), entity.getLastName(), entity.getEmail()));
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        entity.setStatus("Đang làm");
        return userRepository.save(entity);
    }

    @Transactional
    public UserEntity updateAdministrator(AdministratorUpdateDto dto) {
        userRepository.updateAdministrator(dto);
        return userRepository.findByUserId(dto.getUserId());
    }

    @Transactional
    public UserEntity updateDoctor(DoctorUpdateInformationDto dto) {
        userRepository.updateDoctor(dto);
        return Optional.of(userRepository.findByUserId(dto.getUserId()))
                .orElseThrow(() -> new ResourceNotFoundException("Doctor {} not found".replace("{}", dto.getUserId())));
    }

    @Transactional
    public UserEntity updateStatus(String doctorId, String status) {
        userRepository.updateStatus(doctorId, status);
        return Optional.of(userRepository.findByUserId(doctorId))
                .orElseThrow(() -> new ResourceNotFoundException("Doctor {} not found".replace("{}", doctorId)));
    }

    public List<DoctorResponseDto> getDoctorByHospitalId(String hospitalId) {
        Map<String, Double> stars = commonService.getDoctorStar();
        List<UserEntity> entities = userRepository.findAllByHospitalId(hospitalId);
        List<DoctorResponseDto> dtos = entities
                .stream()
                .map(entity -> USER_MAPPER.convertEntityToDto(entity))
                .collect(Collectors.toList());
        for (int i = 0; i < entities.size(); i++) {
            dtos.get(i).setStar(stars.get(dtos.get(i).getUserId()) == null ? 0 : stars.get(dtos.get(i).getUserId()));
            if (entities.get(i).getKnowledge() != null)
                dtos.get(i).setKnowledges(Arrays.stream(entities.get(i).getKnowledge().split(",")).collect(Collectors.toList()));
        }
        return dtos;
    }

    public DoctorInformationResponseDto getDoctorByDoctorId(String doctorId) {
        DoctorResponseDto doctorResponseDto = getAllDoctor().stream()
                .filter(d -> d.getUserId().equals(doctorId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Doctor {} not found".replace("{}", doctorId)));

        DoctorInformationResponseDto doctor = USER_MAPPER.convertDtoToDto(doctorResponseDto);
        HospitalResponseDto hospital = hospitalService.getHospitalByHospitalId(doctor.getHospitalId());
        doctor.setHospitalName(hospital.getHospitalName());
        doctor.setHospitalAddress(hospital.getAddress());
        return doctor;
    }

    public List<DoctorResponseDto> getAllDoctor() {
        Map<String, Double> stars = commonService.getDoctorStar();
        List<UserEntity> entities = userRepository.findAllByDoctorIsTrue();
        List<DoctorResponseDto> dtos = entities
                .stream()
                .map(entity -> USER_MAPPER.convertEntityToDto(entity))
                .collect(Collectors.toList());
        for (int i = 0; i < entities.size(); i++) {
            DoctorResponseDto dto = dtos.get(i);
            dto.setStar(stars.get(dto.getUserId()));
            if (entities.get(i).getKnowledge() != null) {
                dto.setKnowledges(Arrays.stream(entities.get(i).getKnowledge().split(",")).collect(Collectors.toList()));
            }
        }
        dtos.sort(Comparator.nullsLast(Comparator.comparing(DoctorResponseDto::getStar,
                Comparator.nullsFirst(Double::compareTo)).reversed()));

        return dtos;
    }


    /**
     * Administrative
     */
    @Transactional
    public UserEntity saveAdministrative(AdministrativeSaveDto dto) {

        UserEntity entity = USER_MAPPER.convertSaveToEntity(dto);
        entity.setDisable(false);
        entity.setRoleId(3L);
        entity.setUserId(generateUserId(entity.getFirstName(), entity.getLastName(), entity.getEmail()));
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        entity.setStatus("Đang làm");
        return userRepository.save(entity);
    }

    public List<EmployeeResponseDto> getAllEmployeeByHospitalId(String hospitalId) {
        Map<String, Double> stars = commonService.getDoctorStar();
        List<UserEntity> entities = userRepository.findAllEmployeesByHospitalId(hospitalId);
        List<EmployeeResponseDto> dtos = entities.stream()
                .map(entity -> USER_MAPPER.convertEntityToEDto(entity))
                .collect(Collectors.toList());

        for (int i = 0; i < entities.size(); i++) {
            EmployeeResponseDto dto = dtos.get(i);
            dto.setStar(stars.get(dto.getUserId()));
            if (entities.get(i).getKnowledge() != null) {
                dto.setKnowledges(Arrays.stream(entities.get(i).getKnowledge().split(",")).collect(Collectors.toList()));
            }
        }

        // Remove admin
        dtos.removeIf(dto -> dto.getRoleId() == 2);
        return dtos;
    }

    /**
     * Employee
     */
    public UserEntity saveEmployee(EmployeeSaveDto dto) {

        UserEntity entity = USER_MAPPER.convertSaveToEntity(dto);
        entity.setUserId(generateUserId(entity.getFirstName(), entity.getLastName(), entity.getEmail()));
        entity.setStatus("Đang làm");
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        entity.setDisable(false);
        entity.setImageUrl("https://upload.wikimedia.org/wikipedia/commons/thumb/9/98/OOjs_UI_icon_userAvatar.svg/1200px-OOjs_UI_icon_userAvatar.svg.png?fbclid=IwAR2YhMPIGSkRNRbgGk3indgelORexNBxQ358TRKOVIKQ0yZ2HiIwWSLE-9Q");
        entity.setRoleId(dto.isDoctor() ? 4L : 3L);
        return userRepository.save(entity);
    }

    @Transactional
    public void deleteEmployee(String employeeId) {
        userRepository.deleteByUserId(employeeId);
    }

    /**
     * Login
     */

    public LoginResponseDto login(LoginRequest loginRequest) {
        UserEntity entity = userRepository.findByPhone(loginRequest.getPhone());
        if (entity != null && passwordEncoder.matches(loginRequest.getPassword(), entity.getPassword())) {

            LoginResponseDto responseDto = USER_MAPPER.convertEntityToLoginDto(entity);
            responseDto.setKnowledges(entity.getKnowledge() == null ? new ArrayList<>() : Arrays.asList(entity.getKnowledge().split(",")));
            responseDto.setStar(commonService.getDoctorStar().get(entity.getUserId()) == null ? 0 : commonService.getDoctorStar().get(entity.getUserId()));
            responseDto.setHospital(hospitalRepository.getHospitalId(entity.getHospitalId()));
            return responseDto;
        }


        throw new ResourceNotFoundException("User {} not found".replace("{}", loginRequest.getPhone()));
    }

    private List<DoctorGetAllDto> convertProjectionToDto(List<DoctorGetAllProjection> projections) {
        return projections
                .stream()
                .map(projection -> {
                    List<String> knowledges = Arrays.stream(projection.getKnowledge().split(","))
                            .collect(Collectors.toList());
                    DoctorGetAllDto dto = USER_MAPPER.convertProjectToDto(projection);
                    dto.setKnowledges(knowledges);
                    return dto;
                })
                .collect(Collectors.toList());
    }


    /**
     * Private
     */

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


    @Transactional
    public UserEntity changePassword(ChangePasswordDto dto) {
        UserEntity user = userRepository.findByUserId(dto.getUserId());
        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new ResourceNotFoundException("Old password is not valid");
        }
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            throw new ResourceNotFoundException("New password is not matched wiht confirm password");
        }
        userRepository.updatePassword(dto.getUserId(), passwordEncoder.encode(dto.getNewPassword()));
        return userRepository.findByUserId(dto.getUserId());
    }

    public UserCode forgotPassword(String phone) {
        Random random = new Random();
        String id = String.format("%04d", random.nextInt(10000));
        UserEntity user = userRepository.findByPhone(phone);
        emailService.sendSimpleMail(new EmailDetails(user.getEmail(), id, "Your code confirm password is", ""));

        return userCodeService.upsert(user.getUserId(), id);
    }

    public boolean checkCode(String phone, String codeH) {
        UserEntity user = userRepository.findByPhone(phone);
        UserCode code = userCodeRepository.findByUserId(user.getUserId());
        if (!codeH.equals(code.getCode())) {
            return false;
        }
        return true;
    }

    public UserEntity resetPassword(ForgotPasswordDto dto) {
        UserEntity user = userRepository.findByPhone(dto.getPhone());
        UserCode code = userCodeRepository.findByUserId(user.getUserId());

        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            throw new ResourceNotFoundException("Confirm password is not valid");
        }

        userRepository.updatePassword(user.getUserId(), passwordEncoder.encode(dto.getNewPassword()));
        return userRepository.findByUserId(user.getUserId());
    }

    @Transactional
    public void lockUser(String userId) {
        userRepository.lockUser(userId);
    }
}
