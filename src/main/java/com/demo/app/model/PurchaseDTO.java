package com.demo.app.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
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
