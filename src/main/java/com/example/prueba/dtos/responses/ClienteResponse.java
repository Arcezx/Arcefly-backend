package com.example.prueba.dtos.responses;
import java.time.LocalDateTime;
public record ClienteResponse(
        Long id,
        String nombre,
        String email,
        String password,
        String tipo,
        String estado, // Nuevo campo
        LocalDateTime fechaCreacion
) {}