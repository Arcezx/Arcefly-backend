package com.example.prueba.dtos;

public record ClienteDTO(
        Long id,
        String nombre,
        String email,
        String password,
        String tipo,
        String estado
) {}