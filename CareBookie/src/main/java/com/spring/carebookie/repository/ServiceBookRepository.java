package com.spring.carebookie.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.carebookie.entity.ServiceBookEntity;

public interface ServiceBookRepository extends JpaRepository<ServiceBookEntity,Long> {
}
