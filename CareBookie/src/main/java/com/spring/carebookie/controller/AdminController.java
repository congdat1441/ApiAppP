package com.spring.carebookie.controller;

import javax.validation.Valid;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.carebookie.dto.edit.HospitalSettingProfileDto;
import com.spring.carebookie.dto.edit.MedicineUpdateDto;
import com.spring.carebookie.dto.edit.ServiceUpdateDto;
import com.spring.carebookie.dto.edit.WorkingDayDetailEditDto;
import com.spring.carebookie.dto.response.EmployeeResponseDto;
import com.spring.carebookie.dto.save.AdministrativeSaveDto;
import com.spring.carebookie.dto.save.DoctorSaveDto;
import com.spring.carebookie.dto.save.EmployeeSaveDto;
import com.spring.carebookie.dto.save.MedicineSaveDto;
import com.spring.carebookie.dto.save.ServiceSaveDto;
import com.spring.carebookie.dto.save.WorkingDayDetailDto;
import com.spring.carebookie.entity.MedicineEntity;
import com.spring.carebookie.entity.ServiceEntity;
import com.spring.carebookie.entity.UserEntity;
import com.spring.carebookie.entity.WorkingDayDetailsEntity;
import com.spring.carebookie.service.CommonService;
import com.spring.carebookie.service.HospitalService;
import com.spring.carebookie.service.MedicineService;
import com.spring.carebookie.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/care-bookie/admin")
public class AdminController {

    private final UserService userService;

    private final HospitalService hospitalService;

    private final CommonService commonService;

    private final MedicineService medicineService;

    @ApiOperation("Create a doctor")
    @PostMapping("/create/doctor")
    public ResponseEntity<UserEntity> saveDoctor(@Valid @RequestBody DoctorSaveDto dto) {
        return ResponseEntity.ok(userService.saveDoctor(dto));
    }

    @ApiOperation("Create a administrative")
    @PostMapping("/create/administrative")
    public ResponseEntity<UserEntity> saveAdministrative(@Valid @RequestBody AdministrativeSaveDto dto) {
        return ResponseEntity.ok(userService.saveAdministrative(dto));
    }

    @ApiOperation("Create a doctor or administrative")
    @PostMapping("/create/employee")
    public ResponseEntity<UserEntity> saveEmployee(@Valid @RequestBody EmployeeSaveDto dto) {
        return ResponseEntity.ok(userService.saveEmployee(dto));
    }

    @DeleteMapping("/employee/{employeeId}")
    public ResponseEntity<Object> deleteEmployee(@PathVariable String employeeId) {
        userService.deleteEmployee(employeeId);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("Get all employee")
    @GetMapping("/employees/{hospitalId}")
    public ResponseEntity<List<EmployeeResponseDto>> getAllEmployee(@PathVariable String hospitalId) {
        return ResponseEntity.ok(userService.getAllEmployeeByHospitalId(hospitalId));
    }

    @ApiOperation("Add service")
    @PostMapping("/service/create")
    public ResponseEntity<ServiceEntity> createService(@RequestBody ServiceSaveDto dto) {
        return ResponseEntity.ok(commonService.saveService(dto));
    }

    @ApiOperation("Delete service by serviceId")
    @DeleteMapping("/service/delete/{serviceId}")
    public ResponseEntity<Object> deleteService(@PathVariable Long serviceId) {
        commonService.deleteService(serviceId);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("Update service information")
    @PutMapping("/service/update")
    public ResponseEntity<ServiceEntity> updateService(@Valid @RequestBody ServiceUpdateDto dto) {
        return ResponseEntity.ok(commonService.updateService(dto));
    }

    // TODO add workingDays api

    @ApiOperation("Add working day detail of hospital")
    @PostMapping("/working-day/{hospitalId}")
    public ResponseEntity<List<WorkingDayDetailsEntity>> addWorkingDayDetails(@PathVariable String hospitalId,
                                                                              @RequestBody List<WorkingDayDetailDto> dtos) {
        return ResponseEntity.ok(commonService.saveWorkingDayDetail(hospitalId, dtos));
    }

    @ApiOperation("Update information working day of hospital by id")
    @PutMapping("/working-day/{hospitalId}")
    public ResponseEntity<WorkingDayDetailsEntity> updateWorkingDayDetail(@PathVariable String hospitalId, @RequestBody WorkingDayDetailEditDto dto) {
        return ResponseEntity.ok(commonService.updateWorkingDay(hospitalId, dto));
    }

    @ApiOperation("Update information working day of hospital by id")
    @PutMapping("/working-days/{hospitalId}")
    public ResponseEntity<List<WorkingDayDetailsEntity>> updateWorkingDayDetail(@PathVariable String hospitalId, @RequestBody List<WorkingDayDetailEditDto> dtos) {
        return ResponseEntity.ok(commonService.updateWorkingDays(hospitalId, dtos));
    }

    // TODO
    @ApiOperation("Setting profile hospital")
    @PutMapping("/setting/profile")
    public ResponseEntity<?> settingProfile(@RequestBody HospitalSettingProfileDto dto) {
        return ResponseEntity.ok(hospitalService.settingProfile(dto));
    }

    /**
     * Medicine
     */
    @ApiOperation("Add a new medicine into hospital")
    @PostMapping("/medicine")
    public ResponseEntity<MedicineEntity> saveMedicine(@Valid @RequestBody MedicineSaveDto dto) {
        return ResponseEntity.ok(medicineService.save(dto));
    }

    @ApiOperation("Update a medicine into hospital")
    @PutMapping("/medicine")
    public ResponseEntity<MedicineEntity> updateMedicine(@Valid @RequestBody MedicineUpdateDto dto) {
        return ResponseEntity.ok(medicineService.update(dto));
    }

    @ApiOperation("Delete medicine from hospital")
    @DeleteMapping("/medicine/{medicineId}")
    public ResponseEntity<?> deleteMedicine(@PathVariable Long medicineId) {
        medicineService.delete(medicineId);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("Get all medicine by hospital")
    @GetMapping("/medicine/{hospitalId}")
    public ResponseEntity<List<MedicineEntity>> getAllMedicineByHospitalId(@PathVariable String hospitalId) {
        return ResponseEntity.ok(medicineService.getAllMedicineByHospitalId(hospitalId));
    }

    @ApiOperation("Lock medicine")
    @DeleteMapping("/medicine/lock/{medicineId}")
    public ResponseEntity<?> lockMedicine(@PathVariable Long medicineId) {
        medicineService.lockMedicine(medicineId);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("Lock service by serviceId")
    @DeleteMapping("/service/lock/{serviceId}")
    public ResponseEntity<Object> lockService(@PathVariable Long serviceId) {
        commonService.lockService(serviceId);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("Lock employee ")
    @DeleteMapping("/employee/lock/{userId}")
    public ResponseEntity<Object> lockService(@PathVariable String userId) {
        userService.lockUser(userId);
        return ResponseEntity.ok().build();
    }
}
