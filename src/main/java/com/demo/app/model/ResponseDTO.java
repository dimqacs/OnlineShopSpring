package com.demo.app.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResponseDTO {

    private Integer status;

    private String message;

    private Object data;

    public ResponseDTO(Integer status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public ResponseDTO(Integer status, String message){
        this.status = status;
        this.message = message;
    }
}
