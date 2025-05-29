package com.example.prueba.dtos.responses;

import java.time.LocalDate;

public record ReservaResponse(
        Long idReserva,
        Long idViaje,
        Long idUsuario,
        LocalDate fechaReserva,
        String asiento,
        String estado,
        String origen,
        String destino
) {}