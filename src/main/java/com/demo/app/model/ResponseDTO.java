package com.demo.app.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResponseDTO {

    private Integer status;

    private String message;

    private LocalDateTime date;

    public ResponseDTO(Integer status, String message, LocalDateTime date) {
        this.status = status;
        this.message = message;
        this.date = date;
    }
}
