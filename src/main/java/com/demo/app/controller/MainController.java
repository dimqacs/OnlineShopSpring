package com.demo.app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class MainController {
    @GetMapping("/")
    public ResponseEntity<Principal> index(Principal principal) {
        return ResponseEntity.ok(principal);
    }
}
