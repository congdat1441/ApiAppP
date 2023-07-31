package com.spring.carebookie.controller;

import javax.validation.Valid;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.carebookie.dto.edit.AdministratorUpdateDto;
import com.spring.carebookie.dto.edit.BookAcceptDto;
import com.spring.carebookie.dto.edit.BookCancelDto;
import com.spring.carebookie.dto.edit.ConfirmInvoiceDto;
import com.spring.carebookie.dto.response.BookResponseDto;
import com.spring.carebookie.dto.response.InvoiceResponseDto;
import com.spring.carebookie.entity.BookEntity;
import com.spring.carebookie.entity.UserEntity;
import com.spring.carebookie.service.BookService;
import com.spring.carebookie.service.InvoiceService;
import com.spring.carebookie.service.MedicineService;
import com.spring.carebookie.service.UserService;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/care-bookie/administrative")
public class AdministrativeController {

    private final BookService bookService;

    private final UserService userService;

    private final InvoiceService invoiceService;

    private final MedicineService medicineService;

    @ApiOperation("Cancel book")
    @PutMapping("/book/cancel")
    public ResponseEntity<BookResponseDto> cancelBook(@Valid @RequestBody BookCancelDto dto) {
        return ResponseEntity.ok(bookService.cancelBook(dto));
    }

    @ApiOperation("Accept book")
    @PutMapping("/book/accept")
    public ResponseEntity<BookEntity> acceptBook(@Valid @RequestBody BookAcceptDto dto) {
        return ResponseEntity.ok(bookService.acceptBook(dto));
    }

    @ApiOperation("Confirm book")
    @PutMapping("/book/confirm")
    public ResponseEntity<InvoiceResponseDto> confirmBook(@RequestParam Long bookId, @RequestParam String operatorId) {
        return ResponseEntity.ok(bookService.confirmBook(bookId, operatorId));
    }

    @ApiOperation("Get all book with status accept from this day to future")
    @GetMapping("/book/accept/{hospitalId}")
    public ResponseEntity<List<BookResponseDto>> getAllAcceptBookByHospitalId(@PathVariable String hospitalId) {
        return ResponseEntity.ok(bookService.getAllBookAcceptByHospitalId(hospitalId));
    }

    @ApiOperation("Get all book with status pending from this day to future")
    @GetMapping("/book/pending/{hospitalId}")
    public ResponseEntity<List<BookResponseDto>> getAllPendingBookByHospitalId(@PathVariable String hospitalId) {
        return ResponseEntity.ok(bookService.getAllBookPendingByHospitalId(hospitalId));
    }

    @ApiOperation("Get all book with status cancel from this day to future")
    @GetMapping("/book/cancel/{hospitalId}")
    public ResponseEntity<List<BookResponseDto>> getAllCancelBookByHospitalId(@PathVariable String hospitalId) {
        return ResponseEntity.ok(bookService.getAllBookCancelByHospitalId(hospitalId));
    }

    @ApiOperation("Get all book with status confirm from this day to future")
    @GetMapping("/book/confirm/{hospitalId}")
    public ResponseEntity<List<BookResponseDto>> getAllConfirmBookByHospitalId(@PathVariable String hospitalId) {
        return ResponseEntity.ok(bookService.getAllBookConfirmByHospitalId(hospitalId));
    }

    @ApiOperation("Get all invoice by hospitalId with isExamined = false")
    @GetMapping("/invoice/{hospitalId}")
    public ResponseEntity<List<InvoiceResponseDto>> getAllInvoiceByHospitalId(@PathVariable String hospitalId) {
        return ResponseEntity.ok(invoiceService.getAllInvoiceByHospitalId(hospitalId));
    }

    @ApiOperation("Confirm invoice isExamined = true and pass the discount for insurance")
    @PutMapping("/invoice/confirm-examined")
    public ResponseEntity<InvoiceResponseDto> confirmExamined(@RequestBody ConfirmInvoiceDto dto) {
        return ResponseEntity.ok(invoiceService.confirmExamined(dto.getInvoiceId(), dto.getDiscountInsurance()));
    }

    @ApiOperation("Update information for adminstrator")
    @PutMapping("/update/information")
    public ResponseEntity<UserEntity> updateInformation(@Valid @RequestBody AdministratorUpdateDto dto) {
        return ResponseEntity.ok(userService.updateAdministrator(dto));
    }
}
