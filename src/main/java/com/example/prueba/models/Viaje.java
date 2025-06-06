package com.example.prueba.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "viajes")
public class Viaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_viaje")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false, foreignKey = @ForeignKey(name = "fk_viajes_id_usuario"))
    private Usuario usuario;

    @Column(nullable = false, length = 100)
    private String origen;

    @Column(nullable = false, length = 100)
    private String destino;

    @Column(name = "f_salida", nullable = false)
    private LocalDate fechaSalida;

    @Column(name = "f_regreso")
    private LocalDate fechaRegreso;

    @Column(nullable = false)
    private int capacidad;

    @Column(nullable = false, length = 20)
    private String estado;

    @Column(nullable = false, length = 50)
    private String direccion;

    @Column(nullable = false, length = 20)
    private String clase;

    @Column(nullable = false, length = 30)
    private String tipo;
    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt = LocalDateTime.now();
    public Viaje() {}
}