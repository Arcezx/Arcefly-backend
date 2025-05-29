package com.example.prueba.dtos.requests;

public record CreateClienteRequest(
        String nombre,
        String email,
        String password,  // Añadir campo password
        String tipo,
        String estado
) {}