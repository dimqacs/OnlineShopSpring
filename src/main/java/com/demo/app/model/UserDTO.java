package com.demo.app.model;

import lombok.Data;

@Data
public class UserDTO {

    private Long id;

    private String name;

    private String surname;

    private String login;

    private String role;

}
