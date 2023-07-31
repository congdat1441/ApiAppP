package com.spring.carebookie.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.spring.carebookie.entity.InvoiceMedicineEntity;

public interface InvoiceMedicineRepository extends JpaRepository<InvoiceMedicineEntity,Long> {

    @Modifying
    @Query("delete from InvoiceMedicineEntity i where i.invoiceId = ?1 and i.medicineId = ?2")
    void deleteByInvoiceIdAndMedicineId(Long invoiceId, Long medicineId);

}
