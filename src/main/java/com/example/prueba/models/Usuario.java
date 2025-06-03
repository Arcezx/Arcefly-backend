package com.example.prueba.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "usuarios", schema = "login_app")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    private String nombre;
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "password_plain", nullable = true)
    private String passwordPlain;

    @Column(name = "tipo_usuario")
    private String tipoUsuario;

    @Column(name = "estado")
    private String estado = "ACTIVO";
    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum TipoUsuario {
        ADMIN, ESTANDAR, PREMIUM
    }

    public enum Estado {
        ACTIVO, INACTIVO
    }

}