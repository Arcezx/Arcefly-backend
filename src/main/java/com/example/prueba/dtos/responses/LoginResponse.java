package com.example.prueba.dtos.responses;

import com.example.prueba.models.Usuario;
import lombok.Getter;

@Getter
public class LoginResponse {

    private final Long idUsuario;
    private final String nombre;
    private final String tipoUsuario;
    private final String estado;
    private final String email;

    public LoginResponse(Usuario usuario) {
        this.idUsuario = usuario.getIdUsuario();
        this.nombre = usuario.getNombre();
        this.tipoUsuario = usuario.getTipoUsuario();
        this.estado = usuario.getEstado();
        this.email = usuario.getEmail();
    }

}
