package com.spring.carebookie.controller;

import javax.validation.Valid;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.carebookie.dto.edit.BookCancelDto;
import com.spring.carebookie.dto.response.BookResponseDto;
import com.spring.carebookie.dto.response.DoctorAndFavouriteResponseDto;
import com.spring.carebookie.dto.response.HospitalAndFavouriteResponseDto;
import com.spring.carebookie.dto.response.InvoiceResponseDto;
import com.spring.carebookie.dto.save.BookSaveDto;
import com.spring.carebookie.dto.save.CheckConfirmRegisterMail;
import com.spring.carebookie.dto.save.ConfirmRegisterMail;
import com.spring.carebookie.dto.save.RatingDoctorSaveDto;
import com.spring.carebookie.dto.save.RatingHospitalSaveDto;
import com.spring.carebookie.dto.save.RegisterDto;
import com.spring.carebookie.dto.save.UpdateUserInformationDto;
import com.spring.carebookie.entity.RatingDoctorEntity;
import com.spring.carebookie.entity.RatingHospitalEntity;
import com.spring.carebookie.entity.UserCode;
import com.spring.carebookie.entity.UserEntity;
import com.spring.carebookie.entity.UserFavoriteDoctorEntity;
import com.spring.carebookie.entity.UserFavoriteHospitalEntity;
import com.spring.carebookie.service.BookService;
import com.spring.carebookie.service.CommonService;
import com.spring.carebookie.service.InvoiceService;
import com.spring.carebookie.service.UserService;

import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/care-bookie/user")
@CrossOrigin("*")
public class UserController {

    private final CommonService commonService;

    private final BookService bookService;

    private final InvoiceService invoiceService;

    private final UserService userService;

    @ApiOperation("Confirm register mail")
    @PostMapping("/confirm/mail")
    public ResponseEntity<UserCode> confirmMail(@Valid @RequestBody ConfirmRegisterMail dto) {
        return ResponseEntity.ok(userService.confirmRegisterMail(dto));
    }

    @ApiOperation("Confirm register mail")
    @PostMapping("/confirm/mail/check")
    public ResponseEntity<Boolean> checkConfirmMail(@Valid @RequestBody CheckConfirmRegisterMail dto) {
        return ResponseEntity.ok(userService.checkCodeConfirmMail(dto));
    }

    @ApiOperation("User register")
    @PostMapping("/register")
    public ResponseEntity<UserEntity> registerForUser(@Valid @RequestBody RegisterDto dto) {
        return ResponseEntity.ok(userService.register(dto));
    }

    @ApiOperation("User update information")
    @PostMapping("/update/information")
    public ResponseEntity<UserEntity> updateInformationForUser(@Valid @RequestBody UpdateUserInformationDto dto) {
        return ResponseEntity.ok(userService.updateUser(dto));
    }


    @ApiOperation("Create new comment for doctor")
    @PostMapping("comment/doctor")
    public ResponseEntity<RatingDoctorEntity> saveCommentDoctor(@RequestBody RatingDoctorSaveDto dto) {
        return ResponseEntity.ok(commonService.saveRatingDoctor(dto));
    }

    @ApiOperation("Create new comment for hospital")
    @PostMapping("comment/hospital")
    public ResponseEntity<RatingHospitalEntity> saveCommentHospital(@RequestBody RatingHospitalSaveDto dto) {
        return ResponseEntity.ok(commonService.saveRatingHospital(dto));
    }

    @ApiOperation("Create a book")
    @PostMapping("/book")
    public ResponseEntity<BookResponseDto> createNewBook(@Valid @RequestBody BookSaveDto dto) {
        return ResponseEntity.ok(bookService.saveBook(dto));
    }

    @ApiOperation("Cancel book")
    @PutMapping("/book/cancel")
    public ResponseEntity<BookResponseDto> cancelBook(@Valid @RequestBody BookCancelDto dto) {
        return ResponseEntity.ok(bookService.cancelBook(dto));
    }

    @ApiOperation("Get all book with status with pending, cancel, accept, confirm from this day to future")
    @GetMapping("/books/{userId}")
    public ResponseEntity<List<BookResponseDto>> getAllBookByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(bookService.getAllBookByUserId(userId));
    }

    @ApiOperation("Create an favourite doctor for user")
    @PostMapping("/doctor/favourite/{userId}")
    public ResponseEntity<UserFavoriteDoctorEntity> createDoctorFavourite(@PathVariable String userId, @RequestParam String doctorId) {
        return ResponseEntity.ok(commonService.createDoctorFavourite(userId, doctorId));
    }

    @ApiOperation("Create an favourite hospital for user")
    @PostMapping("/hospital/favourite/{userId}")
    public ResponseEntity<UserFavoriteHospitalEntity> createHospitalFavourite(@PathVariable String userId, @RequestParam String hospitalId) {
        return ResponseEntity.ok(commonService.createHospitalFavourite(userId, hospitalId));
    }

    @ApiOperation("Delete an favourite doctor for user")
    @DeleteMapping("/doctor/favourite")
    public ResponseEntity<?> deleteDoctorFavourite(@RequestParam Long id) {
        commonService.deleteDoctorFavourite(id);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("Delete an favourite hospital for user")
    @DeleteMapping("/hospital/favourite")
    public ResponseEntity<?> deleteHospitalFavourite(@RequestParam Long id) {
        commonService.deleteHospitalFavourite(id);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("Get all doctor favourite by doctorId")
    @GetMapping("/doctor/favourite/{userId}")
    public ResponseEntity<List<DoctorAndFavouriteResponseDto>> getAllDoctorFavourite(@PathVariable String userId) {
        return ResponseEntity.ok(commonService.getAllFavouriteDoctorByUserId(userId));
    }

    @ApiOperation("Get all doctor favourite by doctorId")
    @GetMapping("/hospital/favourite/{userId}")
    public ResponseEntity<List<HospitalAndFavouriteResponseDto>> getAllHospitalFavourite(@PathVariable String userId) {
        return ResponseEntity.ok(commonService.getAllFavouriteHospitalByUserId(userId));
    }

    @ApiOperation("Get all invoice by userId")
    @GetMapping("/invoice/{userId}")
    public ResponseEntity<List<InvoiceResponseDto>> getAllInvoiceByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(invoiceService.getAllInvoiceByUserId(userId));
    }

}
