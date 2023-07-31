package com.spring.carebookie.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")
public class UserEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    private String firstName;

    private String lastName;

    private String birthDay;

    @Column(unique = true)
    private String email;

    private int gender;

    @Column(unique = true)
    private String phone;

    private String address;

    private String imageKey;

    private String password;

    private String knowledge;

    private String speciality;

    private String startWorkingDate;

    private String status;

    private boolean isDoctor;

    private boolean isDisable;

    private String hospitalId;

    private Long roleId;

    private String information;

    private String imageUrl;

}
