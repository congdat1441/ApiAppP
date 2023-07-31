package com.spring.carebookie.controller;

import javax.validation.Valid;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.carebookie.dto.CheckCode;
import com.spring.carebookie.dto.ForgotPasswordDto;
import com.spring.carebookie.dto.LoginRequest;
import com.spring.carebookie.dto.SearchHomeResponse;
import com.spring.carebookie.dto.edit.ChangePasswordDto;
import com.spring.carebookie.dto.response.DoctorInformationResponseDto;
import com.spring.carebookie.dto.response.DoctorResponseDto;
import com.spring.carebookie.dto.response.HospitalResponseDto;
import com.spring.carebookie.dto.response.LoginResponseDto;
import com.spring.carebookie.dto.response.RatingDoctorResponseDto;
import com.spring.carebookie.dto.response.RatingHospitalResponseDto;
import com.spring.carebookie.dto.save.RegisterHospital;
import com.spring.carebookie.entity.ServiceEntity;
import com.spring.carebookie.entity.UserCode;
import com.spring.carebookie.entity.UserEntity;
import com.spring.carebookie.entity.WorkingDayDetailsEntity;
import com.spring.carebookie.service.CommonService;
import com.spring.carebookie.service.HospitalService;
import com.spring.carebookie.service.UserService;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/care-bookie/common")
public class CommonController {

    private final CommonService commonService;

    private final HospitalService hospitalService;

    private final UserService userService;

    @ApiOperation("Search home, response will be list of hospital(search by hospitalName and hospitalService) " +
            "and list of doctor (doctorName or speciality)")
    @GetMapping("/search/home")
    public ResponseEntity<SearchHomeResponse> searchHome(@RequestParam String key) {
        return ResponseEntity.ok(commonService.searchHomeByKey(key));
    }

    @ApiOperation("Search hospital by district")
    @GetMapping("/search/district")
    public ResponseEntity<List<HospitalResponseDto>> getHospitalByDistrict(@RequestParam String district) {
        return ResponseEntity.ok(hospitalService.getHospitalByDistrict(district));
    }

    @ApiOperation("Get all hospital include {star, services, workingDayDetails}")
    @GetMapping("/hospital/getAll")
    public ResponseEntity<List<HospitalResponseDto>> getAllHospital() {
        return ResponseEntity.ok(hospitalService.getAllHospitals());
    }

    @ApiOperation("Get hospital by hospitalId include {star, services, workingDayDetails}")
    @GetMapping("/hospital/{hospitalId}")
    public ResponseEntity<HospitalResponseDto> getHospitalById(@PathVariable String hospitalId) {
        return ResponseEntity.ok(hospitalService.getHospitalByHospitalId(hospitalId));
    }

    @ApiOperation("Get all service by hospitalId")
    @GetMapping("/services/{hospitalId}")
    public ResponseEntity<List<ServiceEntity>> getServiceByHospitalId(@PathVariable String hospitalId) {
        return ResponseEntity.ok(commonService.getAllServiceByHospitalId(hospitalId));
    }

    @ApiOperation("Get working day details by hospitalId")
    @GetMapping("/workingDays/{hospitalId}")
    public ResponseEntity<List<WorkingDayDetailsEntity>> getWorkingDayDetailByHospitalId(@PathVariable String hospitalId) {
        return ResponseEntity.ok(commonService.getAllWorkingDayDetailByHospitalId(hospitalId));
    }

    @ApiOperation("Get all comment by hospitalId")
    @GetMapping("/hospital/comment/{hospitalId}")
    public ResponseEntity<List<RatingHospitalResponseDto>> getCommentByHospitalId(@PathVariable String hospitalId) {
        return ResponseEntity.ok(commonService.getAllRatingByHospitalId(hospitalId));
    }

    @ApiOperation("Get all doctor by hospitalId")
    @GetMapping("/doctor/{hospitalId}")
    public ResponseEntity<List<DoctorResponseDto>> getAllDoctorByHospitalId(@PathVariable String hospitalId) {
        return ResponseEntity.ok(userService.getDoctorByHospitalId(hospitalId));
    }

    @ApiOperation("Get information by doctorId")
    @GetMapping("/doctor/information/{doctorId}")
    public ResponseEntity<DoctorInformationResponseDto> getDoctorByDoctorId(@PathVariable String doctorId) {
        return ResponseEntity.ok(userService.getDoctorByDoctorId(doctorId));
    }

    @ApiOperation("Get all doctor and order by star ")
    @GetMapping("/doctor/getAll")
    public ResponseEntity<List<DoctorResponseDto>> getAllDoctorOrderByStar() {
        return ResponseEntity.ok(userService.getAllDoctor());
    }

    @ApiOperation("Get all comment by doctorId ")
    @GetMapping("/doctor/comment/{doctorId}")
    public ResponseEntity<List<RatingDoctorResponseDto>> getCommentByDoctorId(@PathVariable String doctorId) {
        return ResponseEntity.ok(commonService.getAllCommentByDoctorId(doctorId));
    }

    @ApiOperation("Login")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(userService.login(loginRequest));
    }

    @ApiOperation("Get information of account by userId")
    @GetMapping("/user/information/{userId}")
    public ResponseEntity<UserEntity> getUserInformation(@PathVariable String userId) {
        return ResponseEntity.ok(userService.getUserByUserId(userId));
    }

    @ApiOperation("Api register a hospital")
    @PostMapping("/register/hospital")
    public ResponseEntity<?> registerHospital(@Valid @RequestBody RegisterHospital model) {
        return ResponseEntity.ok(hospitalService.registerHospital(model));
    }

    @ApiOperation("Api change password")
    @PostMapping("/user/change-password")
    public ResponseEntity<UserEntity> changePassword(@Valid @RequestBody ChangePasswordDto dto) {
        return ResponseEntity.ok(userService.changePassword(dto));
    }

    @ApiOperation("Forgot password, send mail a code")
    @PostMapping("/user/forgot-password")
    public ResponseEntity<UserCode> forgotPassword(@RequestParam String phone) {
        return ResponseEntity.ok(userService.forgotPassword(phone));
    }

    @ApiOperation("Check code for forgot password")
    @PostMapping("/user/check-code")
    public ResponseEntity<Boolean> checkCode(@RequestBody CheckCode dto) {
        return ResponseEntity.ok(userService.checkCode(dto.getPhone(), dto.getCode()));
    }

    @ApiOperation("Api change password")
    @PostMapping("/user/reset-password")
    public ResponseEntity<UserEntity> changePassword(@Valid @RequestBody ForgotPasswordDto dto) {
        return ResponseEntity.ok(userService.resetPassword(dto));
    }

}
