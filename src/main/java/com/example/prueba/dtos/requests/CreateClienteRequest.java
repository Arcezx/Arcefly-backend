package com.example.prueba.dtos.requests;

public record CreateClienteRequest(
        String nombre,
        String email,
        String password,
        String tipo,
        String estado


) {
    @Override
    public String nombre() {
        return nombre;
    }

    @Override
    public String email() {
        return email;
    }

    @Override
    public String password() {
        return password;
    }

    @Override
    public String tipo() {
        return tipo;
    }

    @Override
    public String estado() {
        return estado;
    }
}