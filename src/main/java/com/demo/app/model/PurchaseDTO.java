package com.demo.app.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PurchaseDTO {

    private Long id;

    private String status;

    private Float total;

    private LocalDateTime createdDate;

    private String userName;

    private String userSurname;

    private String userLogin;

    private Integer userAge;
}
