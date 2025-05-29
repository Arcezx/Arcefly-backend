package com.example.prueba.dtos.requests;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class CreateViajeRequest {
    @NotNull(message = "El ID de usuario es obligatorio")
    private Long idUsuario;

    @NotBlank(message = "El origen no puede estar vacío")
    private String origen;

    @NotBlank(message = "El destino no puede estar vacío")
    private String destino;

    @FutureOrPresent(message = "La fecha de salida debe ser hoy o en el futuro")
    private LocalDate fechaSalida;

    @FutureOrPresent(message = "La fecha de regreso debe ser futura")
    private LocalDate fechaRegreso;  // Opcional (puede ser null)

    @Min(value = 70, message = "La capacidad mínima es 70 pasajeros")
    private int capacidad;

    @NotBlank(message = "El estado no puede estar vacío")
    @Pattern(regexp = "PROGRAMADO|REPROGRAMADO|CANCELADO|EMBARCANDO|ATERRIZADO|EN HORA|POR CONFIRMAR|ACTIVO",
            flags = Pattern.Flag.CASE_INSENSITIVE,
            message = "Estado no válido")
    private String estado;
    @NotBlank(message = "El tipo de trayecto es obligatorio")
    @Pattern(regexp = "IDA|VUELTA|IDA Y VUELTA", message = "Valores permitidos: IDA, VUELTA o IDA Y VUELTA")
    private String direccion;
    @NotBlank(message = "La clase no puede estar vacía")
    private String clase;

    @NotBlank(message = "El tipo no puede estar vacío")
    private String tipo;
}