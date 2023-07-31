package com.spring.carebookie.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.spring.carebookie.entity.InvoiceEntity;
import com.spring.carebookie.entity.ServiceEntity;
import com.spring.carebookie.repository.projection.InvoiceMedicineAmountProjection;
import com.spring.carebookie.repository.projection.TotalInvoiceProjection;

public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Long> {

    @Query(value = "select  s \n" +
            "from InvoiceEntity i \n" +
            "left join InvoiceServiceEntity iser on i.id = iser.invoiceId \n" +
            "join ServiceEntity s on iser.serviceId = s.id \n" +
            "where i.id = :invoiceId")
    List<ServiceEntity> getAllServiceByInvoiceId(Long invoiceId);

    @Query(value = "select  me.id as id,me.hospitalId as hospitalId ,me.medicineName as medicineName,me.medicinePrice as medicinePrice,me.medicineUnit as medicineUnit, ime.amount as amount\n" +
            "from InvoiceEntity i \n" +
            "left join InvoiceMedicineEntity ime on i.id = ime.invoiceId \n" +
            "join MedicineEntity me on ime.medicineId = me.id \n" +
            "where i.id = :invoiceId")
    List<InvoiceMedicineAmountProjection> getAllMedicineByInvoiceId(Long invoiceId);

    @Query("select i from InvoiceEntity i where i.userId = ?1 and i.isExamined = true")
    List<InvoiceEntity> getALlByUserId(String userId);

    @Query("select i from InvoiceEntity i where i.doctorId = :doctorId and i.isExamined = true")
    List<InvoiceEntity> getAllInvoiceDoneByDoctorId(String doctorId);

    @Query("select i from InvoiceEntity i where i.hospitalId = :hospitalId and i.isExamined = true")
    List<InvoiceEntity> getAllInvoiceDoneByHospitalId(String hospitalId);

    @Query("select i from InvoiceEntity i where i.hospitalId = :hospitalId and i.isExamined = true and year(i.dateTimeInvoice) = :year")
    List<InvoiceEntity> getAllInvoiceDoneByHospitalIdAndYear(String hospitalId, Integer year);

    @Query("select i from InvoiceEntity i where i.hospitalId = ?1 and i.isExamined = false ")
    List<InvoiceEntity> getALlByHospitalId(String hospitalId);

    @Query(value = "select i.id as id, sum(s.price) as price\n" +
            "from invoice i\n" +
            "left join invoice_service ise on i.id = ise.invoice_id\n" +
            "left join service s on ise.service_id = s.id\n" +
            "group by i.id", nativeQuery = true)
    List<TotalInvoiceProjection> getTotalByService();

    @Query(value = "select i.id as id, sum(s.medicine_price * ise.amount) as price\n" +
            "from invoice i\n" +
            "left join invoice_medicine ise on i.id = ise.invoice_id\n" +
            "left join medicine s on ise.medicine_id = s.id\n" +
            "group by i.id", nativeQuery = true)
    List<TotalInvoiceProjection> getTotalByMedicine();

    @Modifying
    @Query("update InvoiceEntity i set i.isExamined = true, i.discountInsurance = :discount where i.id = :invoiceId")
    void confirmExamined(Long invoiceId, Double discount);

    @Modifying
    @Query("update InvoiceEntity i set i.diagnose = :diagnose, i.advices = :advices, i.symptomDetail = :symptomDetail where i.id = :invoiceId")
    void updateExamined(Long invoiceId, String diagnose, String advices, String symptomDetail);
}
