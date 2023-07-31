package com.spring.carebookie.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.carebookie.common.mappers.HospitalMapper;
import com.spring.carebookie.common.mappers.RatingDoctorMapper;
import com.spring.carebookie.common.mappers.RatingHospitalMapper;
import com.spring.carebookie.common.mappers.ServiceMapper;
import com.spring.carebookie.common.mappers.UserMapper;
import com.spring.carebookie.common.mappers.WorkingDayDetailMapper;
import com.spring.carebookie.dto.DoctorGetAllDto;
import com.spring.carebookie.dto.HospitalGetAllDto;
import com.spring.carebookie.dto.SearchHomeDoctorResponse;
import com.spring.carebookie.dto.SearchHomeDto;
import com.spring.carebookie.dto.SearchHomeHospitalResponse;
import com.spring.carebookie.dto.SearchHomeResponse;
import com.spring.carebookie.dto.edit.ServiceUpdateDto;
import com.spring.carebookie.dto.edit.WorkingDayDetailEditDto;
import com.spring.carebookie.dto.response.DoctorAndFavouriteResponseDto;
import com.spring.carebookie.dto.response.DoctorResponseDto;
import com.spring.carebookie.dto.response.HospitalAndFavouriteResponseDto;
import com.spring.carebookie.dto.response.HospitalResponseDto;
import com.spring.carebookie.dto.response.RatingDoctorResponseDto;
import com.spring.carebookie.dto.response.RatingHospitalResponseDto;
import com.spring.carebookie.dto.save.RatingDoctorSaveDto;
import com.spring.carebookie.dto.save.RatingHospitalSaveDto;
import com.spring.carebookie.dto.save.ServiceSaveDto;
import com.spring.carebookie.dto.save.WorkingDayDetailDto;
import com.spring.carebookie.entity.HospitalEntity;
import com.spring.carebookie.entity.RatingDoctorEntity;
import com.spring.carebookie.entity.RatingHospitalEntity;
import com.spring.carebookie.entity.ServiceEntity;
import com.spring.carebookie.entity.UserEntity;
import com.spring.carebookie.entity.UserFavoriteDoctorEntity;
import com.spring.carebookie.entity.UserFavoriteHospitalEntity;
import com.spring.carebookie.entity.WorkingDayDetailsEntity;
import com.spring.carebookie.exception.ExistedResourceException;
import com.spring.carebookie.exception.ResourceNotFoundException;
import com.spring.carebookie.repository.HospitalRepository;
import com.spring.carebookie.repository.RatingDoctorRepository;
import com.spring.carebookie.repository.RatingHospitalRepository;
import com.spring.carebookie.repository.ServiceRepository;
import com.spring.carebookie.repository.UserFavouriteDoctorRepository;
import com.spring.carebookie.repository.UserFavouriteHospitalRepository;
import com.spring.carebookie.repository.UserRepository;
import com.spring.carebookie.repository.WorkingDayDetailsRepository;

import io.netty.util.internal.StringUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommonService {

    private final HospitalRepository hospitalRepository;

    private final UserRepository userRepository;

    private final RatingHospitalRepository ratingHospitalRepository;

    private final RatingDoctorRepository ratingDoctorRepository;

    private final ServiceRepository serviceRepository;

    private final WorkingDayDetailsRepository workingDayDetailsRepository;

    private final UserFavouriteDoctorRepository userFavouriteDoctorRepository;

    private final UserFavouriteHospitalRepository userFavouriteHospitalRepository;

    private static final HospitalMapper HOSPITAL_MAPPER = HospitalMapper.INSTANCE;

    private static final UserMapper USER_MAPPER = UserMapper.INSTANCE;

    private static final RatingDoctorMapper RATING_DOCTOR_MAPPER = RatingDoctorMapper.INSTANCE;

    private static final RatingHospitalMapper RATING_HOSPITAL_MAPPER = RatingHospitalMapper.INSTANCE;

    private static final ServiceMapper SERVICE_MAPPER = ServiceMapper.INSTANCE;

    private static final WorkingDayDetailMapper WORKING_DAY_DETAIL_MAPPER = WorkingDayDetailMapper.INSTANCE;

    public SearchHomeResponse searchHomeByKey(String key) {
        List<SearchHomeHospitalResponse> hospitals =
                hospitalRepository.searchByKey(key)
                        .stream()
                        .map(o -> new SearchHomeHospitalResponse(o.getId(), o.getName(), o.getImageUrl(), getHospitalStar().get(o.getId()), o.getAddress()))
                        .collect(Collectors.toList());

        List<SearchHomeDoctorResponse> doctors =
                userRepository.searchByKey(key)
                        .stream()
                        .map(o -> new SearchHomeDoctorResponse(o.getId(), o.getName(), o.getImageUrl(), o.getSpeciality()))
                        .collect(Collectors.toList());
        return new SearchHomeResponse(hospitals, doctors);
    }

    public SearchHomeDto searchByKey(String key) {
        if (StringUtil.isNullOrEmpty(key)) key = null;

        List<DoctorGetAllDto> doctors = userRepository.searchDoctorByKey(key).stream()
                .map(projection -> {
                    List<String> knowledges = Arrays.stream(projection.getKnowledge().split(","))
                            .collect(Collectors.toList());
                    DoctorGetAllDto dto = USER_MAPPER.convertProjectToDto(projection);
                    dto.setKnowledges(knowledges);
                    return dto;
                })
                .collect(Collectors.toList());

        List<HospitalGetAllDto> hospitals = hospitalRepository.searchHospitalByKey(key).stream()
                .map(p -> new HospitalGetAllDto(p, hospitalRepository.getWardsByHospitalId(p.getHospitalId()),
                        hospitalRepository.getAllServiceByHospitalId(p.getHospitalId())))
                .collect(Collectors.toList());

        return new SearchHomeDto(hospitals, doctors);
    }

    /**
     * Service for hospital
     */
    public List<ServiceEntity> getAllServiceByHospitalId(String hospitalId) {
        return serviceRepository.getServiceEntityByHospitalId(hospitalId);
    }

    /**
     * Working day for hospital
     */
    public List<WorkingDayDetailsEntity> getAllWorkingDayDetailByHospitalId(String hospitalId) {
        return workingDayDetailsRepository.findAllByHospitalId(hospitalId);
    }

    /**
     * Rating
     */

    public List<RatingHospitalResponseDto> getAllRatingByHospitalId(String hospitalId) {

        List<RatingHospitalEntity> ratingHospitalEntities = ratingHospitalRepository.getAllByHospitalIdOrderByDateTime(hospitalId);
        List<RatingHospitalResponseDto> ratingResponseDtos = RATING_HOSPITAL_MAPPER.convertEntitiesToDtos(ratingHospitalEntities);
        ratingResponseDtos.forEach(r -> {
            UserEntity user = userRepository.findByUserId(r.getUserId());
            r.setFullName(user.getLastName() + " " + user.getFirstName());
            r.setImageUrl(user.getImageUrl());
        });

        return ratingResponseDtos;
    }

    public List<RatingDoctorResponseDto> getAllCommentByDoctorId(String doctorId) {

        List<RatingDoctorEntity> ratingDoctorEntities = ratingDoctorRepository.getAllByDoctorIdOrderByDateTime(doctorId);
        List<RatingDoctorResponseDto> ratingResponseDtos = RATING_DOCTOR_MAPPER.convertEntitiesToDtos(ratingDoctorEntities);
        ratingResponseDtos.forEach(r -> {
            UserEntity user = userRepository.findByUserId(r.getUserId());
            r.setFullName(user.getLastName() + " " + user.getFirstName());
            r.setImageUrl(user.getImageUrl());
        });

        return ratingResponseDtos;
    }

    public Map<String, Double> getHospitalStar() {

        return ratingHospitalRepository.getHospitalStar().
                stream().collect(Collectors.toMap(r -> r.getId(), r -> validateStar(r.getStar())));
    }

    public Map<String, Double> getDoctorStar() {
        return ratingDoctorRepository.getDoctorStar()
                .stream().collect(Collectors.toMap(r -> r.getId(), r -> validateStar(r.getStar())));
    }

    public RatingDoctorEntity saveRatingDoctor(RatingDoctorSaveDto dto) {
        return ratingDoctorRepository.save(RATING_DOCTOR_MAPPER.convertDtoToEntity(dto));
    }

    public RatingHospitalEntity saveRatingHospital(RatingHospitalSaveDto dto) {
        return ratingHospitalRepository.save(RATING_HOSPITAL_MAPPER.convertDtoToEntity(dto));
    }

    /**
     * Service
     */
    @Transactional
    public ServiceEntity saveService(ServiceSaveDto dto) {
        ServiceEntity entity = SERVICE_MAPPER.convertSaveToEntity(dto);
        return serviceRepository.save(entity);
    }

    @Transactional
    public void deleteService(Long serviceId) {
        serviceRepository.deleteById(serviceId);
    }

    @Transactional
    public ServiceEntity updateService(ServiceUpdateDto dto) {
        serviceRepository.updateService(dto.getServiceId(), dto.getServiceName(), dto.getPrice());
        return serviceRepository.findById(dto.getServiceId()).orElseThrow(() ->
                new ResourceNotFoundException("Service {} not found".replace("{}", dto.getServiceId().toString())));
    }

    /**
     * WorkingDayDetail
     */
    @Transactional
    public List<WorkingDayDetailsEntity> saveWorkingDayDetail(String hospitalId, List<WorkingDayDetailDto> dayDetailDtos) {
        List<WorkingDayDetailsEntity> entities = WORKING_DAY_DETAIL_MAPPER.convertSaveDtosToEntities(dayDetailDtos);
        entities.forEach(w -> w.setHospitalId(hospitalId));
        return workingDayDetailsRepository.saveAll(entities);
    }

    @Transactional
    public WorkingDayDetailsEntity updateWorkingDay(String hospitalId, WorkingDayDetailEditDto dto) {
        workingDayDetailsRepository.updateWorkingDay(hospitalId, dto.getId(), dto.getStartHour(), dto.getEndHour());
        return workingDayDetailsRepository.findById(dto.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Working day {} not found".replace("{}", dto.getId().toString()))
        );
    }

    @Transactional
    public List<WorkingDayDetailsEntity> updateWorkingDays(String hospitalId, List<WorkingDayDetailEditDto> dtos) {
        dtos.forEach(dto -> workingDayDetailsRepository.updateWorkingDay(hospitalId, dto.getId(), dto.getStartHour(), dto.getEndHour()));
        return getAllWorkingDayDetailByHospitalId(hospitalId);
    }

    /**
     * Favourite
     */

    @Transactional
    public UserFavoriteDoctorEntity createDoctorFavourite(String userId, String doctorId) {
        // Check condition
        if (userFavouriteDoctorRepository.existsByDoctorIdAndUserId(doctorId, userId)) {
            throw new ExistedResourceException("The doctor {} is favourite before".replace("{}", doctorId));
        }
        return userFavouriteDoctorRepository.save(new UserFavoriteDoctorEntity(null, userId, doctorId));
    }

    @Transactional
    public void deleteDoctorFavourite(Long id) {
        userFavouriteDoctorRepository.deleteById(id);
    }

    @Transactional
    public UserFavoriteHospitalEntity createHospitalFavourite(String userId, String hospitalId) {
        if (userFavouriteHospitalRepository.existsByHospitalIdAndUserId(hospitalId, userId)) {
            throw new ExistedResourceException("The hospital {} is favourite before".replace("{}", hospitalId));
        }
        return userFavouriteHospitalRepository.save(new UserFavoriteHospitalEntity(null, userId, hospitalId));
    }

    @Transactional
    public void deleteHospitalFavourite(Long id) {
        userFavouriteHospitalRepository.deleteById(id);
    }

    public List<DoctorAndFavouriteResponseDto> getAllFavouriteDoctorByUserId(String userId) {

        Map<String, Double> stars = getDoctorStar();

        List<String> doctorId = userFavouriteDoctorRepository.getAllFavouriteDoctorIdByUserId(userId);
        List<Long> favouriteId = userFavouriteDoctorRepository.getAllFavouriteIdByUserId(userId);
        List<UserEntity> doctor = new ArrayList<>();
        doctor.addAll(doctorId.stream()
                .map(d -> userRepository.findByUserId(d))
                .collect(Collectors.toList()));
        List<DoctorResponseDto> dtos = doctor
                .stream()
                .map(entity -> USER_MAPPER.convertEntityToDto(entity))
                .collect(Collectors.toList());
        for (int i = 0; i < doctor.size(); i++) {
            dtos.get(i).setStar(stars.get(dtos.get(i).getUserId()) == null ? 0 : stars.get(dtos.get(i).getUserId()));
            if (doctor.get(i).getKnowledge() != null)
                dtos.get(i).setKnowledges(Arrays.stream(doctor.get(i).getKnowledge().split(",")).collect(Collectors.toList()));
        }

        List<DoctorAndFavouriteResponseDto> result = new ArrayList<>();

        for (int i = 0; i < favouriteId.size(); i++) {
            result.add(new DoctorAndFavouriteResponseDto(dtos.get(i), favouriteId.get(i)));
        }
        return result;

    }

    public List<HospitalAndFavouriteResponseDto> getAllFavouriteHospitalByUserId(String userId) {
        Map<String, Double> stars = getHospitalStar();

        List<Long> favouriteId = userFavouriteHospitalRepository.getAllFavouriteIdByUserId(userId);

        List<String> hospitalIds = userFavouriteHospitalRepository.getAllFavouriteHospitalIdByUserId(userId);

        List<HospitalEntity> hospitalEntities = hospitalRepository.getAllByHospitalId(hospitalIds);

        List<HospitalResponseDto> dtos = HOSPITAL_MAPPER.convertEntitiesToDtos(hospitalEntities);
        dtos.forEach(dto -> {
            dto.setStar(stars.get(dto.getHospitalId()) == null ? 0 : stars.get(dto.getHospitalId()));
            dto.setServices(getAllServiceByHospitalId(dto.getHospitalId()));
            dto.setWorkingDayDetails(getAllWorkingDayDetailByHospitalId(dto.getHospitalId()));
            dto.setAdminInformation(userRepository.findByUserId(dto.getAdminId()));
        });
        dtos.sort(Comparator.nullsLast(Comparator.comparing(HospitalResponseDto::getStar,
                Comparator.nullsFirst(Double::compareTo)).reversed()));

        List<HospitalAndFavouriteResponseDto> result = new ArrayList<>();

        for (int i = 0; i < favouriteId.size(); i++) {
            result.add(new HospitalAndFavouriteResponseDto(dtos.get(i), favouriteId.get(i)));
        }
        return result;
    }


    /**
     * Private
     */
    private Double validateStar(Double star) {
        return star % star.intValue() >= 0.5 ? (double) star.intValue() + 1 : (double) star.intValue();
    }

    @Transactional
    public void lockService(Long serviceId) {
        serviceRepository.lockService(serviceId);
    }
}
