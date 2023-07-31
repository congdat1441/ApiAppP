package com.spring.carebookie.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.carebookie.dto.edit.MedicineRemoveInvoiceDto;
import com.spring.carebookie.dto.edit.ServiceRemoveInvoiceDto;
import com.spring.carebookie.dto.response.BookAndInvoiceStatistic;
import com.spring.carebookie.dto.response.InvoiceResponseDto;
import com.spring.carebookie.dto.response.StatisticBookResponse;
import com.spring.carebookie.dto.response.StatisticDoctorResponse;
import com.spring.carebookie.dto.response.StatisticResponse;
import com.spring.carebookie.dto.response.UserInvoiceResponse;
import com.spring.carebookie.dto.save.InvoiceSaveDto;
import com.spring.carebookie.entity.BookEntity;
import com.spring.carebookie.entity.HospitalEntity;
import com.spring.carebookie.entity.InvoiceEntity;
import com.spring.carebookie.entity.InvoiceMedicineEntity;
import com.spring.carebookie.entity.InvoiceServiceEntity;
import com.spring.carebookie.entity.ServiceEntity;
import com.spring.carebookie.entity.UserEntity;
import com.spring.carebookie.exception.ResourceNotFoundException;
import com.spring.carebookie.repository.BookRepository;
import com.spring.carebookie.repository.HospitalRepository;
import com.spring.carebookie.repository.InvoiceMedicineRepository;
import com.spring.carebookie.repository.InvoiceRepository;
import com.spring.carebookie.repository.InvoiceServiceRepository;
import com.spring.carebookie.repository.ServiceRepository;
import com.spring.carebookie.repository.UserRepository;
import com.spring.carebookie.repository.projection.DoctorGetAllProjection;
import com.spring.carebookie.repository.projection.InvoiceMedicineAmountProjection;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    private final HospitalRepository hospitalRepository;

    private final InvoiceServiceRepository invoiceServiceRepository;

    private final UserRepository userRepository;

    private final CommonService commonService;

    private final BookRepository bookRepository;

    private final ServiceRepository serviceRepository;

    private final InvoiceMedicineRepository invoiceMedicineRepository;

    public BookAndInvoiceStatistic statisticByHospitalId(String hospitalId, int year) {
        List<InvoiceEntity> invoiceEntities = invoiceRepository.getAllInvoiceDoneByHospitalIdAndYear(hospitalId, year);
        List<InvoiceResponseDto> invoice = getInvoiceByIdCommon(invoiceEntities);
        Set<Map.Entry<Integer, List<InvoiceResponseDto>>> invoiceSet = invoice.stream()
                .collect(Collectors.groupingBy(t -> t.getInvoiceInformation().getDateTimeInvoice().getMonth().getValue()))
                .entrySet();
        Map<Integer, StatisticResponse> map = createMap();

        for (Map.Entry<Integer, List<InvoiceResponseDto>> ivS : invoiceSet) {
            double totalPriceByMonth = 0d;

            double totalService = 0d;
            for (InvoiceResponseDto iv : ivS.getValue()) {
                totalPriceByMonth += iv.getTotalPrice();
                double totalPriceService = 0d;
                for (ServiceEntity s : iv.getServices()) {
                    totalPriceService += s.getPrice().doubleValue();
                }
                // each invoice in month
                totalPriceService = totalPriceService - (totalPriceService * (iv.getInvoiceInformation().getDiscountInsurance() / 100));

                // month
                totalService += totalPriceService;
            }

            map.replace(ivS.getKey(), new StatisticResponse(totalPriceByMonth, Math.floor(totalService * 100) / 100, Math.floor((totalPriceByMonth - totalService) * 100) / 100));
        }

        // book
        List<BookEntity> book = bookRepository.getAllBookByHpAndYear(hospitalId, year);
        Set<Map.Entry<Integer, List<BookEntity>>> bookSet = book.stream()
                .collect(Collectors.groupingBy(b -> b.getDateTimeBook().getMonth().getValue()))
                .entrySet();
        Map<Integer, StatisticBookResponse> mapB = createBookMap();

        for (Map.Entry<Integer, List<BookEntity>> bs : bookSet) {
            int numberOfBook = bs.getValue().size();
            int cancel = (int)
                    bs.getValue().stream().filter(b -> b.getStatus().equals("CANCEL"))
                            .count();
            int confirm = numberOfBook - cancel;

            double cancelPercent = ((double) cancel / numberOfBook) * 100;
            double confirmPercent = ((double) confirm / numberOfBook) * 100;
            mapB.replace(bs.getKey(), new StatisticBookResponse(numberOfBook, cancel, Math.floor(cancelPercent * 100) / 100, confirm, Math.floor(confirmPercent * 100) / 100));
        }

        // Doctor and book
        Map<String, StatisticDoctorResponse> mapD = createDoctorMap(hospitalId);
        Set<Map.Entry<String, List<BookEntity>>> bookD = book.stream()
                .collect(Collectors.groupingBy(t -> t.getDoctorId()))
                .entrySet();
        for (Map.Entry<String, List<BookEntity>> bd : bookD) {
            UserEntity doctor = userRepository.findByUserId(bd.getKey());
            String doctorName = doctor.getLastName() + " " + doctor.getFirstName();
            int numberOfBook = bd.getValue().size();
            int cancel = (int)
                    bd.getValue().stream().filter(b -> b.getStatus().equals("CANCEL")).count();
            int confirm = numberOfBook - cancel;
            mapD.replace(doctorName, new StatisticDoctorResponse(numberOfBook,cancel,confirm));
        }

        BookAndInvoiceStatistic result = new BookAndInvoiceStatistic();
        result.setInvoices(map);
        result.setBooks(mapB);
        result.setDoctors(mapD);
        return result;
    }

    public List<InvoiceResponseDto> getAllInvoiceDoneByDoctorId(String doctorId) {
        List<InvoiceEntity> invoiceEntities = invoiceRepository.getAllInvoiceDoneByDoctorId(doctorId);
        return getInvoiceByIdCommon(invoiceEntities);
    }

    public List<InvoiceResponseDto> getAllInvoiceDoneByHospitalId(String hospitalId) {
        List<InvoiceEntity> invoiceEntities = invoiceRepository.getAllInvoiceDoneByHospitalId(hospitalId);
        return getInvoiceByIdCommon(invoiceEntities);
    }

    public InvoiceResponseDto getAllInvoiceDoneByHospitalIdAndBookId(String hospitalId, Long bookId) {
        return getAllInvoiceDoneByHospitalId(hospitalId)
                .stream()
                .filter(t -> t.getInvoiceInformation().getBookId().equals(bookId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Not found invoice with bookId = {}"
                        .replace("{}", bookId.toString())));
    }

    public InvoiceResponseDto getInvoiceByHospitalIdAndUserId(String hospitalId, String userId) {
        return getAllInvoiceByHospitalId(hospitalId)
                .stream()
                .filter(i -> i.getInvoiceInformation().getUserId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));
    }

    @Transactional
    public InvoiceResponseDto updateInvoice(InvoiceSaveDto dto) {

        InvoiceEntity invoice = invoiceRepository.findById(dto.getInvoiceId())
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));

        InvoiceResponseDto invoiceRp = convertEntityToResponse(invoice);

        List<Long> serviceIdDto = dto.getServices()
                .stream()
                .map(t -> t.getServiceId())
                .collect(Collectors.toList());

        List<Long> serviceIdE = invoiceRp.getServices()
                .stream()
                .map(t -> t.getId())
                .collect(Collectors.toList());

        for (Long id : serviceIdDto) {
            if (serviceIdE.contains(id)) {
                throw new ResourceNotFoundException("Service id = {} existed in invoice".replace("{}", id.toString()));
            }
        }

        List<Long> medicineIdDto = dto.getMedicines()
                .stream()
                .map(t -> t.getMedicineId())
                .collect(Collectors.toList());

        List<Long> medicineIdE = invoiceRp.getMedicines()
                .stream()
                .map(t -> t.getId())
                .collect(Collectors.toList());

        for (Long id : medicineIdDto) {
            if (medicineIdE.contains(id)) {
                throw new ResourceNotFoundException("Medicine id = {} existed in invoice".replace("{}", id.toString()));
            }
        }
        // add service
        List<InvoiceServiceEntity> services = dto.getServices()
                .stream()
                .map(d -> new InvoiceServiceEntity(null, dto.getInvoiceId(), d.getServiceId()))
                .collect(Collectors.toList());

        invoiceServiceRepository.saveAll(services);

        // add medicine
        List<InvoiceMedicineEntity> medicines = dto.getMedicines()
                .stream()
                .map(d -> new InvoiceMedicineEntity(null, dto.getInvoiceId(), d.getMedicineId(), d.getAmount()))
                .collect(Collectors.toList());

        invoiceMedicineRepository.saveAll(medicines);

        invoiceRepository.updateExamined(dto.getInvoiceId(), dto.getDiagnose(), dto.getAdvices(), dto.getSymptomDetail());

        InvoiceEntity i = invoiceRepository.findById(dto.getInvoiceId())
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));

        return convertEntityToResponse(i);

    }

    @Transactional
    public InvoiceResponseDto removeService(ServiceRemoveInvoiceDto dto) {
        invoiceServiceRepository.deleteByInvoiceIdAndMedicineId(dto.getInvoiceId(), dto.getServiceId());
        InvoiceEntity i = invoiceRepository.findById(dto.getInvoiceId())
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));

        return convertEntityToResponse(i);
    }

//    @Transactional
//    public InvoiceResponseDto addService(ServiceIntoInvoiceDto dto) {
//        InvoiceServiceEntity entity = invoiceServiceRepository.save(new InvoiceServiceEntity(null, dto.getInvoiceId(), dto.getServiceId()));
//
//        if (entity == null) {
//            throw new RuntimeException("Can not add service $1 into invoice $2"
//                    .replace("$1", dto.getServiceId().toString())
//                    .replace("$2", dto.getInvoiceId().toString()));
//        }
//
//        InvoiceEntity i = invoiceRepository.findById(dto.getInvoiceId())
//                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));
//
//        return convertEntityToResponse(i);
//    }

    @Transactional
    public InvoiceResponseDto confirmExamined(Long invoiceId, Double discountInsurance) {

        invoiceRepository.confirmExamined(invoiceId, discountInsurance);

        InvoiceEntity i = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));

        return convertEntityToResponse(i);

    }

    @Transactional
    public InvoiceResponseDto removeMedicine(MedicineRemoveInvoiceDto dto) {
        invoiceMedicineRepository.deleteByInvoiceIdAndMedicineId(dto.getInvoiceId(), dto.getMedicineId());
        InvoiceEntity i = invoiceRepository.findById(dto.getInvoiceId())
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));

        return convertEntityToResponse(i);
    }

//    @Transactional
//    public InvoiceResponseDto addMedicine(MedicineIntoInvoiceDto dto) {
//
//        InvoiceMedicineEntity entity = invoiceMedicineRepository
//                .save(new InvoiceMedicineEntity(null, dto.getInvoiceId(), dto.getMedicineId(), dto.getAmount()));
//
//        if (entity == null) {
//            throw new RuntimeException("Can not add medicine $1 into invoice $2"
//                    .replace("$1", dto.getMedicineId().toString())
//                    .replace("$2", dto.getInvoiceId().toString()));
//        }
//
//        InvoiceEntity i = invoiceRepository.findById(dto.getInvoiceId())
//                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));
//
//        return convertEntityToResponse(i);
//    }

    public List<InvoiceResponseDto> getAllInvoiceByHospitalId(String hospitalId) {
        List<InvoiceEntity> invoices = invoiceRepository.getALlByHospitalId(hospitalId);
        return getInvoiceByIdCommon(invoices);
    }

    public List<InvoiceResponseDto> getAllInvoiceByDoctorId(String hospitalId, String doctorId) {
        List<InvoiceEntity> invoices = invoiceRepository.getALlByHospitalId(hospitalId)
                .stream()
                .filter(i -> i.getDoctorId().equals(doctorId))
                .collect(Collectors.toList());
        return getInvoiceByIdCommon(invoices);
    }

    public List<InvoiceResponseDto> getAllInvoiceByUserId(String userId) {
        List<InvoiceEntity> invoices = invoiceRepository.getALlByUserId(userId);
        invoices.sort(Comparator.comparing(InvoiceEntity::getDateTimeInvoice,Comparator.reverseOrder()));
        return getInvoiceByIdCommon(invoices);
    }

    public List<InvoiceResponseDto> getInvoiceByIdCommon(List<InvoiceEntity> invoices) {
        List<InvoiceResponseDto> invoiceResponseDtos = new ArrayList<>();
        invoices.forEach(i -> {
            invoiceResponseDtos.add(convertEntityToResponse(i));
        });
        return invoiceResponseDtos;
    }

    public Map<Long, Double> getInvoicePriceService() {

        Map<Long, Double> services = new HashMap<>();
        invoiceRepository.getTotalByService()
                .forEach(t -> {
                    if (t.getId() != null && t.getPrice() != null) {
                        services.put(t.getId(), t.getPrice());
                    }
                });
        return services;
    }

    public Map<Long, Double> getInvoicePriceMedicine() {
        Map<Long, Double> medicines = new HashMap<>();
        invoiceRepository.getTotalByMedicine()
                .forEach(t -> {
                    if (t.getId() != null && t.getPrice() != null) {
                        medicines.put(t.getId(), t.getPrice());
                    }
                });
        return medicines;
    }


    public InvoiceResponseDto convertEntityToResponse(InvoiceEntity i) {

        Map<Long, Double> servicePrice = getInvoicePriceService();
        Map<Long, Double> medicinePrice = getInvoicePriceMedicine();
        List<ServiceEntity> serviceInvoice = invoiceRepository.getAllServiceByInvoiceId(i.getId());
        List<InvoiceMedicineAmountProjection> medicineInvoice = invoiceRepository.getAllMedicineByInvoiceId(i.getId());

        HospitalEntity hospital = hospitalRepository.getHospitalId(i.getHospitalId());
        String hospitalName = hospital.getHospitalName();
        String address = hospital.getAddress();
        Double star = commonService.getHospitalStar().get(i.getHospitalId());
        String imageUrl = hospital.getImageUrl();

        UserEntity doctor = userRepository.findByUserId(i.getDoctorId());
        String doctorName = doctor == null ? null : doctor.getLastName() + " " + doctor.getFirstName();

        UserInvoiceResponse user = new UserInvoiceResponse();
        BookEntity b = bookRepository.findById(i.getBookId()).orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        UserEntity e = userRepository.findByUserId(i.getUserId());
        String[] bd = e.getBirthDay().split("-");
        int year = LocalDate.now().getYear() - Integer.parseInt(bd[2]);

        user.setFullNameBook(b.getName());
        user.setAgeBook(b.getAge());
        user.setGenderBook(b.getGender());
        user.setUserId(e.getUserId());
        user.setFullName(e.getLastName() + " " + e.getFirstName());
        user.setGender(e.getGender() == 1 ? "Nam" : "Nữ");
        user.setAge(year);

        UserEntity operator = userRepository.findByUserId(i.getOperatorId());
        String operatorName = "";
        if (operator != null) {
            operatorName = operator.getLastName() + " " + operator.getFirstName();
        }

        Double totalPrice = (servicePrice.get(i.getId()) == null ? 0 : servicePrice.get(i.getId())) +
                (medicinePrice.get(i.getId()) == null ? 0 : medicinePrice.get(i.getId()));

        totalPrice = totalPrice - (totalPrice * (i.getDiscountInsurance() / 100));
        return new InvoiceResponseDto(user, hospitalName, address, star, imageUrl, doctorName, operatorName, totalPrice,
                i, serviceInvoice, medicineInvoice);
    }

    public Map<Integer, StatisticResponse> createMap() {
        Map<Integer, StatisticResponse> map = new HashMap<>();
        for (int i = 1; i < 13; i++) {
            map.put(i, new StatisticResponse(0d, 0d, 0d));
        }
        return map;
    }

    public Map<Integer, StatisticBookResponse> createBookMap() {
        Map<Integer, StatisticBookResponse> map = new HashMap<>();
        for (int i = 1; i < 13; i++) {
            map.put(i, new StatisticBookResponse());
        }
        return map;
    }

    public Map<String, StatisticDoctorResponse> createDoctorMap(String hospitalId) {
        List<UserEntity> doctor = userRepository.getAllDoctorByHospitalId(hospitalId);
        Map<String, StatisticDoctorResponse> map = new HashMap<>();
        for (UserEntity d : doctor) {
            map.put(d.getLastName() + " " + d.getFirstName(), new StatisticDoctorResponse());
        }
        return map;
    }

    public Map<String, Integer> createServiceMap(String hospitalId) {
        List<ServiceEntity> services = serviceRepository.getServiceEntityByHospitalId(hospitalId);
        Map<String, Integer> map = new HashMap<>();
        for (ServiceEntity d : services) {
            map.put(d.getServiceName(), 0);
        }
        return map;
    }
}
