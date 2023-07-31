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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.carebookie.dto.response.HospitalResponseDto;
import com.spring.carebookie.dto.save.HospitalSaveDto;
import com.spring.carebookie.entity.HospitalEntity;
import com.spring.carebookie.entity.UserEntity;
import com.spring.carebookie.service.HospitalService;
import com.spring.carebookie.service.UserService;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/care-bookie/super-admin")
public class SupperAdminController {

    private final HospitalService hospitalService;

    private final UserService userService;

    @ApiOperation("Create an hospital")
    @PostMapping("/hospital/save")
    public ResponseEntity<HospitalResponseDto> save(@Valid @RequestBody HospitalSaveDto dto) {
        return ResponseEntity.ok(hospitalService.saveHospital(dto));
    }

    @ApiOperation("Get all users include user, employee, admin")
    @GetMapping("/users")
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @ApiOperation("Get all users (patients)")
    @GetMapping("/users/patient")
    public ResponseEntity<List<UserEntity>> getAllPatients() {
        return ResponseEntity.ok(userService.getAllPatients());
    }

    @ApiOperation("Accept the hospital working")
    @PutMapping("/hospital/accept/{hospitalId}")
    public ResponseEntity<HospitalResponseDto> acceptHospital(@PathVariable String hospitalId, @RequestParam boolean accept) {
        return ResponseEntity.ok(hospitalService.acceptHospital(hospitalId, accept));
    }

    @ApiOperation("Delete hospital, delete all table relationship with hospital")
    @DeleteMapping("/hospital/delete/{hospitalId}")
    public ResponseEntity<?> deleteHospital(@PathVariable String hospitalId) {
        hospitalService.deleteHospital(hospitalId);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("Delete user")
    @DeleteMapping("/user/delete/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable String userId) {
        userService.deleteEmployee(userId);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("Lock user")
    @DeleteMapping("/user/lock/{userId}")
    public ResponseEntity<?> lockUser(@PathVariable String userId) {
        userService.lockUser(userId);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("Delete hospital, delete all table relationship with hospital")
    @DeleteMapping("/hospital/lock/{hospitalId}")
    public ResponseEntity<?> lockHospital(@PathVariable String hospitalId) {
        hospitalService.lockHospital(hospitalId);
        return ResponseEntity.ok().build();
    }
}
