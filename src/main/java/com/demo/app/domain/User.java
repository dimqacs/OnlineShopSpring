package com.demo.app.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false, updatable = false)
    private String name;

    @Column(nullable = false, updatable = false)
    private String surname;

    private Integer age;

    @Column(nullable = false, updatable = false, unique = true)
    private String login;

    @Column(nullable = false, updatable = false)
    private String password;

    private String role;

    @Column(nullable = false, updatable = false)
    private LocalDateTime registredDate;

    @Column(nullable = false, updatable = false)
    private LocalDateTime lastSeenDate;

    public User (){
        this.role = "user";
        this.lastSeenDate = LocalDateTime.now();
    }
}