package com.demo.app.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpDTO {

    private String name;

    private String surname;

    private Integer age;

    private String login;

    private String password;
}
