package com.example.prueba.dtos.requests;

public record UpdateReservaRequest(
        Long idViaje,
        Long idUsuario,
        String asiento,
        String estado
) {}