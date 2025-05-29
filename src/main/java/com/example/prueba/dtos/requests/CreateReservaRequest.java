package com.example.prueba.dtos.requests;

import jakarta.validation.constraints.*;

public record CreateReservaRequest(
        @NotNull(message = "El ID del viaje es obligatorio")
        @Positive(message = "El ID del viaje debe ser positivo")
        Long idViaje,

        @NotNull(message = "El ID del usuario es obligatorio")
        @Positive(message = "El ID del usuario debe ser positivo")
        Long idUsuario,

        @NotBlank(message = "El número de asiento es obligatorio")
        @Size(min = 1, max = 10, message = "El asiento debe tener entre 1 y 10 caracteres")
        String asiento
) {}
