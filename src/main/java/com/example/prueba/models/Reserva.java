package com.example.prueba.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Setter
@Getter
@Entity
@Table(name = "reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reserva")
    private Long idReserva;  // Cambiado de Integer a Long

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_viaje", insertable = false, updatable = false)
    private Viaje viaje;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", insertable = false, updatable = false)
    private Usuario usuario;

    @Column(name = "id_viaje", nullable = false)
    private Long idViaje;  // Cambiado de Integer a Long

    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;  // Cambiado de Integer a Long

    @Column(name = "f_reserva", nullable = false)
    private LocalDate fechaReserva;

    @Column(name = "asiento", nullable = false, length = 10)
    private String asiento;

    @Column(name = "estado", nullable = false, length = 20)
    private String estado = "POR CONFIRMAR";
}
