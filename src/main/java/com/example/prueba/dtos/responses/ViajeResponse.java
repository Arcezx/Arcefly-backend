package com.example.prueba.dtos.responses;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ViajeResponse {
    private Long idViaje;
    private Long idUsuario;
    private String origen;
    private String destino;
    private LocalDate fechaSalida;
    private LocalDate fechaRegreso;
    private String direccion;
    private String clase;
    private String tipo;
    private Integer capacidad;
    private String estado;
    private boolean esSoloIda; // Campo calculado

    // Opcional: Si quieres mostrar el nombre del usuario
    private String nombreUsuario;
}