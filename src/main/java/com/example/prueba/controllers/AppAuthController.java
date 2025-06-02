package com.example.prueba.controllers;


import com.example.prueba.dtos.requests.AppLoginRequest;
import com.example.prueba.dtos.responses.AppLoginResponse;
import com.example.prueba.services.AppAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/app/auth")
public class AppAuthController {

    @Autowired
    private AppAuthService appAuthService;

    @PostMapping("/login")
    public ResponseEntity<AppLoginResponse> login(@RequestBody AppLoginRequest request) {
        AppLoginResponse response = appAuthService.authenticate(request);
        return ResponseEntity.ok(response);
    }
}