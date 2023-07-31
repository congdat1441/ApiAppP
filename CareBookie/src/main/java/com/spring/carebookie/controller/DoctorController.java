package com.spring.carebookie.controller;

import javax.validation.Valid;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.carebookie.dto.edit.DoctorUpdateInformationDto;
import com.spring.carebookie.dto.edit.MedicineRemoveInvoiceDto;
import com.spring.carebookie.dto.edit.ServiceRemoveInvoiceDto;
import com.spring.carebookie.dto.response.BookResponseDto;
import com.spring.carebookie.dto.response.InvoiceResponseDto;
import com.spring.carebookie.dto.response.StatisticResponse;
import com.spring.carebookie.dto.save.InvoiceSaveDto;
import com.spring.carebookie.entity.MedicineEntity;
import com.spring.carebookie.entity.ServiceEntity;
import com.spring.carebookie.entity.UserEntity;
import com.spring.carebookie.service.BookService;
import com.spring.carebookie.service.InvoiceService;
import com.spring.carebookie.service.MedicineService;
import com.spring.carebookie.service.ServiceService;
import com.spring.carebookie.service.UserService;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/care-bookie/doctor")
public class DoctorController {

    private final UserService userService;

    private final BookService bookService;

    private final InvoiceService invoiceService;

    private final MedicineService medicineService;

    private final ServiceService serviceService;


    @ApiOperation("Get all book doctorId and status is ACCEPT")
    @GetMapping("/book/accept")
    public ResponseEntity<List<BookResponseDto>> getAllBookAcceptedByDoctorId(@RequestParam String doctorId) {
        return ResponseEntity.ok(bookService.getAllBookAcceptByDoctorId(doctorId));
    }

    @ApiOperation("Get all book doctorId and status is ACCEPT")
    @GetMapping("/book/confirm")
    public ResponseEntity<List<BookResponseDto>> getAllBookConfirmByDoctorId(@RequestParam String doctorId) {
        return ResponseEntity.ok(bookService.getAllBookConfirmByDoctorId(doctorId));
    }

    @ApiOperation("Update doctor information")
    @PutMapping("/update/information")
    public ResponseEntity<UserEntity> updateInformation(@Valid @RequestBody DoctorUpdateInformationDto dto) {
        return ResponseEntity.ok(userService.updateDoctor(dto));
    }

    @ApiOperation("Update status")
    @PutMapping("/update/status/{doctorId}")
    public ResponseEntity<UserEntity> updateStatus(@PathVariable String doctorId, @RequestParam String status) {
        return ResponseEntity.ok(userService.updateStatus(doctorId, status));
    }

    @ApiOperation("Get all invoice by  with isExamined = false")
    @GetMapping("/invoice/{hospitalId}/{doctorId}")
    public ResponseEntity<List<InvoiceResponseDto>> getAllInvoiceByDoctorId(@PathVariable String hospitalId,
                                                                            @PathVariable String doctorId) {
        return ResponseEntity.ok(invoiceService.getAllInvoiceByDoctorId(hospitalId, doctorId));
    }

    @ApiOperation("Live search medicine by name ")
    @GetMapping("/medicine/search/{hospitalId}")
    public ResponseEntity<List<MedicineEntity>> searchMedicineByKey(@PathVariable String hospitalId, @RequestParam String name) {
        return ResponseEntity.ok(medicineService.search(hospitalId, name));
    }

//    // TODO  Add medicine into invoice
//    @ApiOperation("Add medicine into invoice with invoiceId")
//    @PostMapping("/invoice/add/medicine")
//    public ResponseEntity<InvoiceResponseDto> addMedicineIntoInvoice(@RequestBody MedicineIntoInvoiceDto dto) {
//        return ResponseEntity.ok(invoiceService.addMedicine(dto));
//    }

    @ApiOperation("Remove medicine from invoice")
    @DeleteMapping("/invoice/remove/medicine")
    public ResponseEntity<InvoiceResponseDto> removeMedicineFromInvoice(@RequestBody MedicineRemoveInvoiceDto dto) {
        return ResponseEntity.ok(invoiceService.removeMedicine(dto));
    }

    @ApiOperation("Live search medicine by name ")
    @GetMapping("/service/search/{hospitalId}")
    public ResponseEntity<List<ServiceEntity>> searchServiceByKey(@PathVariable String hospitalId, @RequestParam String name) {
        return ResponseEntity.ok(serviceService.search(hospitalId, name));
    }

    // TODO Add service into invoice

//    @ApiOperation("Add service into invoice with invoiceId")
//    @PostMapping("/invoice/add/service")
//    public ResponseEntity<InvoiceResponseDto> addServiceIntoInvoice(@RequestBody ServiceIntoInvoiceDto dto) {
//        return ResponseEntity.ok(invoiceService.addService(dto));
//    }

    @ApiOperation("Remove service from invoice")
    @DeleteMapping("/invoice/remove/service")
    public ResponseEntity<InvoiceResponseDto> removeServiceFromInvoice(@RequestBody ServiceRemoveInvoiceDto dto) {
        return ResponseEntity.ok(invoiceService.removeService(dto));
    }

    @ApiOperation("Api update all information of invoice for doctor include {symptomDetail, advices, diagnose} ( Recommend )")
    @PutMapping("/invoice/update")
    public ResponseEntity<InvoiceResponseDto> updateInvoice(@RequestBody InvoiceSaveDto dto) {
        return ResponseEntity.ok(invoiceService.updateInvoice(dto));
    }

    @ApiOperation("Api get all history invoice by doctorId")
    @GetMapping("/invoice/history")
    public ResponseEntity<List<InvoiceResponseDto>> getHistoryInvoiceByDoctorId(@RequestParam String doctorId) {
        return ResponseEntity.ok(invoiceService.getAllInvoiceDoneByDoctorId(doctorId));
    }
}
