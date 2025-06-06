package com.example.prueba.controllers;

import com.example.prueba.dtos.requests.LoginRequest;
import com.example.prueba.dtos.responses.LoginResponse;
import com.example.prueba.models.Usuario;
import com.example.prueba.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("/api/app/auth")
public class AppAuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;


    public AppAuthController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findByEmail(loginRequest.getEmail());

        if (optionalUsuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("message", "Correo o contraseña incorrectos"));
        }

        Usuario usuario = optionalUsuario.get();

        if (!passwordEncoder.matches(loginRequest.getPassword(), usuario.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("message", "Correo o contraseña incorrectos"));
        }

        LoginResponse response = new LoginResponse(usuario);
        return ResponseEntity.ok(response);
    }

}