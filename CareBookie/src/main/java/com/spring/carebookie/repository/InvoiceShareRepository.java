package com.spring.carebookie.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.carebookie.entity.InvoiceShareEntity;

public interface InvoiceShareRepository extends JpaRepository<InvoiceShareEntity,Long> {
}
