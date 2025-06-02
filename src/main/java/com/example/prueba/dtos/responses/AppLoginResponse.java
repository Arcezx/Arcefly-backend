package com.example.prueba.dtos.responses;

public class AppLoginResponse {
    private Long idUsuario;
    private String nombre;
    private String tipoUsuario;
    private String estado;
    private String email;

    public AppLoginResponse(Long idUsuario, String nombre, String tipoUsuario, String estado, String email) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.tipoUsuario = tipoUsuario;
        this.estado = estado;
        this.email = email;
    }
    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
