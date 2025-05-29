package com.example.prueba.controllers;

import com.example.prueba.models.Usuario;
import com.example.prueba.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario loginUser) {
        try {
            Map<String, Object> authResponse = authService.authenticateUser(
                    loginUser.getEmail(),
                    loginUser.getPassword()
            );

            if ((boolean) authResponse.get("success")) {
                return ResponseEntity.ok(authResponse);
            } else {
                return ResponseEntity.status(401).body(authResponse);
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "success", false,
                    "message", "Error en el servidor"
            ));
        }
    }
}