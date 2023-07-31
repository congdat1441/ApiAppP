package com.spring.carebookie.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.spring.carebookie.entity.InvoiceServiceEntity;

public interface InvoiceServiceRepository extends JpaRepository<InvoiceServiceEntity, Long> {
    @Modifying
    @Query("delete from InvoiceServiceEntity i where i.invoiceId = :invoiceId and i.serviceId = :serviceId")
    void deleteByInvoiceIdAndMedicineId(Long invoiceId, Long serviceId);
}
