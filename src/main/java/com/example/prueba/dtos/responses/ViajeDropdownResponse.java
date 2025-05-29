package com.example.prueba.dtos.responses;

import java.time.LocalDate;

public record ViajeDropdownResponse(
        Long id,
        String descripcion,
        LocalDate fechaSalida
) {}