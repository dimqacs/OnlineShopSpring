package com.demo.app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/")
public class MainController {

    @GetMapping
    public String userAcces(Principal principal){
        if(principal == null)
            return null;
        return principal.getName();
    }

}
