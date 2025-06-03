package com.example.prueba.services;

import com.example.prueba.models.Usuario;
import com.example.prueba.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    public AuthService(UsuarioRepository usuarioRepository,
                       PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public Map<String, Object> authenticateUser(String email, String password) {
        Map<String, Object> response = new HashMap<>();

        try {
            Optional<Usuario> userOptional = usuarioRepository.findByEmail(email);

            if (userOptional.isEmpty()) {
                response.put("success", false);
                response.put("message", "Usuario no encontrado");
                return response;
            }

            Usuario user = userOptional.get();

            if (!passwordEncoder.matches(password, user.getPassword())) {
                response.put("success", false);
                response.put("message", "Credenciales incorrectas");
                return response;
            }

            user.setLastLogin(LocalDateTime.now());
            user.setEstado("ACTIVO");
            usuarioRepository.save(user);

            response.put("success", true);
            response.put("user", Map.of(
                    "id", user.getIdUsuario(),
                    "nombre", user.getNombre(),
                    "email", user.getEmail(),
                    "tipoUsuario", user.getTipoUsuario(),
                    "last_login", user.getLastLogin(),
                    "estado", user.getEstado()
            ));
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error en el servidor: " + e.getMessage());
        }

        return response;
    }
}