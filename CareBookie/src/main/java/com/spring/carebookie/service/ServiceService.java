package com.spring.carebookie.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.spring.carebookie.entity.ServiceEntity;
import com.spring.carebookie.repository.ServiceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ServiceService {

    private final ServiceRepository serviceRepository;

    public List<ServiceEntity> search(String hospitalId, String name) {
        return serviceRepository.search(hospitalId, name);
    }
}
