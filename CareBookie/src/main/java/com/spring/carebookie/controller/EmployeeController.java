package com.spring.carebookie.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.carebookie.dto.response.BookResponseDto;
import com.spring.carebookie.dto.response.InvoiceResponseDto;
import com.spring.carebookie.dto.response.StatisticBookResponse;
import com.spring.carebookie.dto.response.StatisticResponse;
import com.spring.carebookie.service.BookService;
import com.spring.carebookie.service.InvoiceService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/care-bookie/employee")
public class EmployeeController {

    private final InvoiceService invoiceService;

    private final BookService bookService;

    @ApiOperation("Get detail user invoice by userId")
    @GetMapping("/invoice/detail/{hospitalId}/{userId}")
    public ResponseEntity<InvoiceResponseDto> getDetailByUserId(@PathVariable String hospitalId, @PathVariable String userId) {
        return ResponseEntity.ok(invoiceService.getInvoiceByHospitalIdAndUserId(hospitalId, userId));
    }

    @ApiOperation("Get detail book")
    @GetMapping("/book/pending/detail/{hospitalId}/{bookId}")
    public ResponseEntity<BookResponseDto> getBookPending(@PathVariable String hospitalId, @PathVariable Long bookId) {
        return ResponseEntity.ok(bookService.getBookDetailPending(hospitalId, bookId));
    }

    @ApiOperation("Get detail book")
    @GetMapping("/book/accept/detail/{hospitalId}/{bookId}")
    public ResponseEntity<BookResponseDto> getBookAccept(@PathVariable String hospitalId, @PathVariable Long bookId) {
        return ResponseEntity.ok(bookService.getBookDetailAccept(hospitalId, bookId));
    }

    @ApiOperation("Get detail book")
    @GetMapping("/book/confirm/detail/{hospitalId}/{bookId}")
    public ResponseEntity<BookResponseDto> getBookConfirm(@PathVariable String hospitalId, @PathVariable Long bookId) {
        return ResponseEntity.ok(bookService.getBookDetailConfirm(hospitalId, bookId));
    }

    @ApiOperation("Api get all history invoice by hospitalId")
    @GetMapping("/invoice/history")
    public ResponseEntity<List<InvoiceResponseDto>> getHistoryInvoiceByHospitalId(@RequestParam String hospitalId) {
        return ResponseEntity.ok(invoiceService.getAllInvoiceDoneByHospitalId(hospitalId));
    }

    @ApiOperation("Api get history invoice by hospitalId and bookId")
    @GetMapping("/invoice/history/details")
    public ResponseEntity<InvoiceResponseDto> getHistoryInvoiceByHospitalIdAndBookId(@RequestParam String hospitalId, @RequestParam Long bookId) {
        return ResponseEntity.ok(invoiceService.getAllInvoiceDoneByHospitalIdAndBookId(hospitalId, bookId));
    }

    @ApiOperation("Get statistic revenue and book by hospital")
    @GetMapping("/statistic/revue/book/{hospitalId}")
    public ResponseEntity<?> statisticRevenueAndBook(@PathVariable String hospitalId, @RequestParam int year) {
        return ResponseEntity.ok(invoiceService.statisticByHospitalId(hospitalId,year));
    }

}