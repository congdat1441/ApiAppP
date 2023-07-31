package com.spring.carebookie.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.ResourceAccessException;

import com.spring.carebookie.common.constants.BookStatus;
import com.spring.carebookie.common.mappers.BookMapper;
import com.spring.carebookie.dto.edit.BookAcceptDto;
import com.spring.carebookie.dto.edit.BookCancelDto;
import com.spring.carebookie.dto.response.BookResponseDto;
import com.spring.carebookie.dto.response.InvoiceResponseDto;
import com.spring.carebookie.dto.save.BookSaveDto;
import com.spring.carebookie.entity.BookEntity;
import com.spring.carebookie.entity.HospitalEntity;
import com.spring.carebookie.entity.InvoiceEntity;
import com.spring.carebookie.entity.InvoiceShareEntity;
import com.spring.carebookie.entity.ServiceBookEntity;
import com.spring.carebookie.entity.ServiceEntity;
import com.spring.carebookie.entity.UserEntity;
import com.spring.carebookie.exception.BookDateNotValidException;
import com.spring.carebookie.exception.ResourceNotFoundException;
import com.spring.carebookie.repository.BookRepository;
import com.spring.carebookie.repository.HospitalRepository;
import com.spring.carebookie.repository.InvoiceMedicineRepository;
import com.spring.carebookie.repository.InvoiceRepository;
import com.spring.carebookie.repository.InvoiceServiceRepository;
import com.spring.carebookie.repository.InvoiceShareRepository;
import com.spring.carebookie.repository.MedicineRepository;
import com.spring.carebookie.repository.ServiceBookRepository;
import com.spring.carebookie.repository.ServiceRepository;
import com.spring.carebookie.repository.UserRepository;
import com.spring.carebookie.repository.projection.InvoiceMedicineAmountProjection;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService {

    private final HospitalRepository hospitalRepository;

    private final InvoiceService invoiceService;

    private final UserRepository userRepository;

    private final BookRepository bookRepository;

    private final InvoiceShareRepository invoiceShareRepository;

    private final ServiceBookRepository serviceBookRepository;

    private final ServiceRepository serviceRepository;

    private final CommonService commonService;

    private final InvoiceRepository invoiceRepository;

    private final InvoiceMedicineRepository invoiceMedicineRepository;

    private final InvoiceServiceRepository invoiceServiceRepository;

    private final MedicineRepository medicineRepository;

    private static final BookMapper BOOK_MAPPER = BookMapper.INSTANCE;

    public BookResponseDto getBookDetailPending(String hospitalId, Long bookId) {
        return getAllBookPendingByHospitalId(hospitalId)
                .stream()
                .filter(b -> b.getBookInformation().getHospitalId().equals(hospitalId)
                        && b.getBookInformation().getId().equals(bookId))
                .findAny()
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
    }

    public BookResponseDto getBookDetailAccept(String hospitalId, Long bookId) {
        return getAllBookAcceptByHospitalId(hospitalId)
                .stream()
                .filter(b -> b.getBookInformation().getHospitalId().equals(hospitalId)
                        && b.getBookInformation().getId().equals(bookId))
                .findAny()
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
    }

    public BookResponseDto getBookDetailConfirm(String hospitalId, Long bookId) {
        return getAllBookConfirmByHospitalId(hospitalId)
                .stream()
                .filter(b -> b.getBookInformation().getHospitalId().equals(hospitalId)
                        && b.getBookInformation().getId().equals(bookId))
                .findAny()
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
    }

    @Transactional
    public BookResponseDto saveBook(BookSaveDto dto) {

        if (dto.getDateExamination().isBefore(LocalDate.now())) {
            throw new BookDateNotValidException("The date " + dto.getDateExamination() + " is not before now");
        }
        BookEntity entity = BOOK_MAPPER.convertSaveDtoToEntity(dto);
        entity.setStatus(BookStatus.PENDING.toString());
        entity.setDateTimeBook(LocalDateTime.now());

        BookEntity saveBook = bookRepository.save(entity);
        List<InvoiceResponseDto> invoiceResponseDtos = new ArrayList<>();
        List<ServiceEntity> serviceBooks = new ArrayList<>();
        //add service in
        List<ServiceBookEntity> serviceBookEntities = new ArrayList<>();
        if (dto.getServices() != null && dto.getServices().size() > 0) {
            dto.getServices()
                    .forEach(s -> serviceBookEntities.add(new ServiceBookEntity(null, saveBook.getId(), s)));
            serviceBookRepository.saveAll(serviceBookEntities);
            // Response list of service book entity
            serviceBooks = serviceRepository.findAllById(dto.getServices());
        }

        List<InvoiceShareEntity> invoiceShares = new ArrayList<>();
        if (dto.isShareInvoice() && dto.getInvoices().size() > 0) {
            // add invoice share
            dto.getInvoices()
                    .forEach(s -> invoiceShares.add(new InvoiceShareEntity(null, saveBook.getId(), s)));
            invoiceShareRepository.saveAll(invoiceShares);

            // Response list of invoice share include list of medicine and list of service share
            List<InvoiceEntity> invoices = invoiceRepository.findAllById(dto.getInvoices());

            invoices.forEach(i -> {
                List<ServiceEntity> serviceInvoice = invoiceRepository.getAllServiceByInvoiceId(i.getId());
                List<InvoiceMedicineAmountProjection> medicineInvoice = invoiceRepository.getAllMedicineByInvoiceId(i.getId());
                invoiceResponseDtos.add(new InvoiceResponseDto(i, serviceInvoice, medicineInvoice));
            });
        }

        BookResponseDto bookResponseDto = new BookResponseDto(saveBook, serviceBooks, invoiceResponseDtos);
        HospitalEntity hospital = hospitalRepository.getHospitalId(dto.getHospitalId());
        bookResponseDto.setHospitalNameH(hospital.getHospitalName());
        bookResponseDto.setAddressH(hospital.getAddress());
        bookResponseDto.setImageUrlH(hospital.getImageUrl());
        bookResponseDto.setStarH(commonService.getHospitalStar().get(hospital.getHospitalId()));


        UserEntity doctor = userRepository.findByUserId(dto.getDoctorId());
        String doctorName = doctor == null ? "" : doctor.getLastName() + " " + doctor.getFirstName();
        UserEntity patient = userRepository.findByUserId(dto.getUserId());
        String fullName = patient.getLastName() + " " + patient.getFirstName();

        bookResponseDto.setDoctorName(doctorName);
        bookResponseDto.setFullName(fullName);
        bookResponseDto.setUserId(dto.getUserId());
        bookResponseDto.setGender(patient.getGender());
        bookResponseDto.setAddress(patient.getAddress());
        String[] bd = patient.getBirthDay().split("-");
        int year = LocalDate.now().getYear() - Integer.parseInt(bd[2]);
        bookResponseDto.setAge(year);

        return bookResponseDto;
    }

    @Transactional
    public BookEntity acceptBook(BookAcceptDto dto) {
        String date = (dto.getDateExamination().getDayOfWeek().getValue() + 1) + "";
        bookRepository.acceptBook(dto.getBookId(), dto.getDoctorId(), date, dto.getDateExamination(), dto.getSession(), dto.getOperatorId(), dto.getMessage());
        return bookRepository.findById(dto.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book {} not found"
                        .replace("{}", dto.getBookId().toString())));
    }

    @Transactional
    public InvoiceResponseDto confirmBook(Long bookId, String operatorId) {

        // TODO create an invoice for bookId
        bookRepository.confirmBook(bookId, operatorId);

        BookEntity b = bookRepository.findById(bookId).orElseThrow(() ->
                new ResourceNotFoundException("Book {} not found".replace("{}", bookId.toString())));

        // Create an invoice for this book;
        InvoiceEntity invoice = new InvoiceEntity();
        invoice.setBookId(b.getId());
        invoice.setDateTimeInvoice(LocalDateTime.now());
        invoice.setUserId(b.getUserId());
        invoice.setHospitalId(b.getHospitalId());
        invoice.setOperatorId(b.getOperationId());
        invoice.setDoctorId(b.getDoctorId());
        invoice.setDiscountInsurance(0D);
        invoice.setExamined(false);

        invoiceRepository.save(invoice);
        // return 1 ResponseInvoiceDto theo bookId and hospitalId
        InvoiceResponseDto invoiceResponseDto = invoiceService.getAllInvoiceByHospitalId(invoice.getHospitalId())
                .stream()
                .filter(i -> i.getInvoiceInformation().getBookId().equals(bookId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Not found invoice"));

        return invoiceResponseDto;
    }

    @Transactional
    public BookResponseDto cancelBook(BookCancelDto dto) {

        bookRepository.cancelBook(dto.getBookId(), dto.getMessage(), dto.getOperatorId());
        BookEntity bookEntity = bookRepository.findById(dto.getBookId())
                .orElseThrow(() ->
                        new ResourceAccessException("Not found book with {}"
                                .replace("{}", dto.getBookId().toString())));

        List<Long> serviceBookIds = bookRepository.getServiceIdsByBookId(dto.getBookId());

        // Service
        List<ServiceEntity> serviceBooks = new ArrayList<>();
        if (serviceBookIds.size() > 0) {
            serviceBooks = serviceRepository.findAllById(serviceBookIds);
        }

        // Invoice share
        List<InvoiceResponseDto> invoiceResponseDtos = new ArrayList<>();
        if (bookEntity.isShareInvoice()) {
            List<Long> invoiceShareIds = bookRepository.getInvoiceShareIdsByBookId(dto.getBookId());

            // Response list of invoice share include list of medicine and list of service share
            List<InvoiceEntity> invoices = invoiceRepository.findAllById(invoiceShareIds);

            invoices.forEach(i -> {
                List<ServiceEntity> serviceInvoice = invoiceRepository.getAllServiceByInvoiceId(i.getId());
                List<InvoiceMedicineAmountProjection> medicineInvoice = invoiceRepository.getAllMedicineByInvoiceId(i.getId());
                invoiceResponseDtos.add(new InvoiceResponseDto(i, serviceInvoice, medicineInvoice));
            });
        }
        return new BookResponseDto(bookEntity, serviceBooks, invoiceResponseDtos);
    }

    public List<BookResponseDto> getAllBookAcceptByDoctorId(String doctorId) {
        List<BookEntity> bookEntities = bookRepository.getBookAcceptByDoctorId(doctorId);
        return getAllBookByStatus(bookEntities);
    }

    public List<BookResponseDto> getAllBookConfirmByDoctorId(String doctorId) {
        List<BookEntity> bookEntities = bookRepository.getBookConfirmByDoctorId(doctorId);
        return getAllBookByStatus(bookEntities);
    }

    public List<BookResponseDto> getAllBookPendingByHospitalId(String hospitalId) {
        List<BookEntity> bookEntities = bookRepository.getAllBookPendingByHospitalId(hospitalId);
        return getAllBookByStatus(bookEntities);
    }

    public List<BookResponseDto> getAllBookAcceptByHospitalId(String hospitalId) {
        List<BookEntity> bookEntities = bookRepository.getAllBookAcceptByHospitalId(hospitalId);
        return getAllBookByStatus(bookEntities);
    }

    public List<BookResponseDto> getAllBookCancelByHospitalId(String hospitalId) {
        List<BookEntity> bookEntities = bookRepository.getAllBookCancelByHospitalId(hospitalId);
        return getAllBookByStatus(bookEntities);
    }

    public List<BookResponseDto> getAllBookConfirmByHospitalId(String hospitalId) {
        List<BookEntity> bookEntities = bookRepository.getAllBookConfirmByHospitalId(hospitalId);
        return getAllBookByStatus(bookEntities);
    }

    public List<BookResponseDto> getAllBookByUserId(String userId) {
        List<BookEntity> bookEntities = bookRepository.getAllBookByUserId(userId);
        bookEntities.sort(Comparator.comparing(BookEntity::getDateExamination));
        return getAllBookByStatus(bookEntities);
    }

    /**
     * All status
     *
     * @param bookEntities
     * @return
     */
    private List<BookResponseDto> getAllBookByStatus(List<BookEntity> bookEntities) {
        List<BookResponseDto> bookResponseDtos = new ArrayList<>();

        // ignore book date after date now
        bookEntities.removeIf(b -> b.getDateExamination().isBefore(LocalDate.now()));

        bookEntities.forEach(b -> {
            List<Long> serviceBookIds = bookRepository.getServiceIdsByBookId(b.getId());
            // Service
            List<ServiceEntity> serviceBooks = new ArrayList<>();
            if (serviceBookIds.size() > 0) {
                serviceBooks = serviceRepository.findAllById(serviceBookIds);
            }

            // Invoice share
            List<InvoiceResponseDto> invoiceResponseDtos = new ArrayList<>();
            if (b.isShareInvoice()) {
                List<Long> invoiceShareIds = bookRepository.getInvoiceShareIdsByBookId(b.getId());

                // Response list of invoice share include list of medicine and list of service share
                List<InvoiceEntity> invoices = invoiceRepository.findAllById(invoiceShareIds);

                Map<Long, Double> servicePrice = invoiceService.getInvoicePriceService();
                Map<Long, Double> medicinePrice = invoiceService.getInvoicePriceMedicine();

                invoices.forEach(i -> {
                    List<ServiceEntity> serviceInvoice = invoiceRepository.getAllServiceByInvoiceId(i.getId());
                    List<InvoiceMedicineAmountProjection> medicineInvoice = invoiceRepository.getAllMedicineByInvoiceId(i.getId());
                    InvoiceResponseDto invoice = new InvoiceResponseDto(servicePrice.get(i.getId()) == null ? 0 : servicePrice.get(i.getId()) +
                            (medicinePrice.get(i.getId()) == null ? 0 : medicinePrice.get(i.getId())), i, serviceInvoice, medicineInvoice);

                    invoice.setHospitalName(hospitalRepository.getHospitalId(i.getHospitalId()).getHospitalName());
                    invoiceResponseDtos.add(invoice);
                });
            }

            BookResponseDto bookResponseDto = new BookResponseDto(b, serviceBooks, invoiceResponseDtos);
            HospitalEntity hospital = hospitalRepository.getHospitalId(b.getHospitalId());
            bookResponseDto.setHospitalNameH(hospital.getHospitalName());
            bookResponseDto.setAddressH(hospital.getAddress());
            bookResponseDto.setImageUrlH(hospital.getImageUrl());
            bookResponseDto.setStarH(commonService.getHospitalStar().get(hospital.getHospitalId()));


            UserEntity doctor = userRepository.findByUserId(b.getDoctorId());
            String doctorName = doctor == null ? "" : doctor.getLastName() + " " + doctor.getFirstName();
            UserEntity patient = userRepository.findByUserId(b.getUserId());
            String fullName = patient.getLastName() + " " + patient.getFirstName();

            bookResponseDto.setDoctorName(doctorName);
            bookResponseDto.setFullName(fullName);
            bookResponseDto.setUserId(b.getUserId());
            bookResponseDto.setGender(patient.getGender());
            bookResponseDto.setAddress(patient.getAddress());
            String[] bd = patient.getBirthDay().split("-");
            int year = LocalDate.now().getYear() - Integer.parseInt(bd[2]);
            bookResponseDto.setAge(year);

            UserEntity operator = userRepository.findByUserId(b.getOperationId());
            bookResponseDto.setOperatorId(b.getOperationId());
            bookResponseDto.setFullNameOperator(operator == null ? "" : operator.getLastName() + " " + operator.getFirstName());
            bookResponseDtos.add(bookResponseDto);
        });

        return bookResponseDtos;
    }

}
