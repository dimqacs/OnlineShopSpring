package com.demo.app.model;

import lombok.Data;

@Data
public class SignUpDTO {

    private String name;

    private String surname;

    private Integer age;

    private String login;

    private String password;
}
