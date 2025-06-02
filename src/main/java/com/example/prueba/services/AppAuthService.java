package com.example.prueba.services;

import com.example.prueba.dtos.requests.AppLoginRequest;
import com.example.prueba.dtos.responses.AppLoginResponse;
import com.example.prueba.models.Usuario;
import com.example.prueba.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class AppAuthService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AppAuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AppLoginResponse authenticate(AppLoginRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        usuario.setLastLogin(LocalDateTime.now());
        usuarioRepository.save(usuario);

        if (!"ACTIVO".equalsIgnoreCase(usuario.getEstado())) {
            throw new RuntimeException("Tu cuenta no está activa. Por favor contacta al administrador.");
        }

        return new AppLoginResponse(
                usuario.getIdUsuario(),
                usuario.getNombre(),
                usuario.getTipoUsuario(),
                usuario.getEstado(),
                usuario.getEmail()
        );
    }
}