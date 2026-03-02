package com.jakob.secure_case_management_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/api/test/secure")
    public ResponseEntity<String> secure() {
        return ResponseEntity.ok("You are authenticated");
    }
}