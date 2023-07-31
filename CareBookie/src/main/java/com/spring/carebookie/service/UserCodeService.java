package com.spring.carebookie.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.carebookie.entity.UserCode;
import com.spring.carebookie.repository.UserCodeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserCodeService {

    private final UserCodeRepository userCodeRepository;

    @Transactional
    public UserCode upsert(String userId, String code) {
        if (userCodeRepository.existsUserCodeByUserId(userId)) {
            userCodeRepository.update(userId, code);
            return userCodeRepository.findByUserId(userId);
        }
        return userCodeRepository.save(new UserCode(null, userId, code));
    }

}
