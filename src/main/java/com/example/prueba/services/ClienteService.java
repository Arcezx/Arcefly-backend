package com.example.prueba.services;

import com.example.prueba.dtos.ClienteDTO;
import com.example.prueba.dtos.requests.CreateClienteRequest;
import com.example.prueba.models.Usuario;
import com.example.prueba.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ClienteService {
    private final UsuarioRepository usuarioRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ClienteService(UsuarioRepository usuarioRepo, PasswordEncoder passwordEncoder) {
        this.usuarioRepo = usuarioRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario crearCliente(CreateClienteRequest request) {
        if (usuarioRepo.existsByEmail(request.email())) {
            throw new RuntimeException("Email ya registrado");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(request.nombre());
        usuario.setEmail(request.email());
        usuario.setTipoUsuario(request.tipo());
        usuario.setEstado(request.estado() != null ? request.estado() : "ACTIVO");

        // Solo hashear la contraseña si se proporciona
        if (request.password() != null && !request.password().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(request.password()));
            usuario.setPasswordPlain(request.password());
        } else {
            // Generar una contraseña temporal si no se proporciona
            String tempPassword = "temp" + UUID.randomUUID().toString().substring(0, 8);
            usuario.setPassword(passwordEncoder.encode(tempPassword));
            usuario.setPasswordPlain(tempPassword);
        }

        return usuarioRepo.save(usuario);
    }
    public List<Usuario> listarClientes() {
        return usuarioRepo.findByTipoUsuarioIn(List.of("ESTANDAR", "PREMIUM"));
    }

    public List<Usuario> buscarClientes(String filtro) {
        return usuarioRepo.findByNombreContainingIgnoreCaseOrEmailContainingIgnoreCase(filtro, filtro)
                .stream()
                .filter(u -> List.of("ESTANDAR", "PREMIUM").contains(u.getTipoUsuario()))
                .collect(Collectors.toList());
    }

    public Usuario actualizarCliente(Long id, ClienteDTO dto) {
        Usuario usuario = usuarioRepo.findById(id).orElseThrow();
        usuario.setNombre(dto.nombre());
        usuario.setEmail(dto.email());
        usuario.setTipoUsuario(dto.tipo());
        usuario.setEstado(dto.estado());

        // Actualizar contraseña solo si se proporciona una nueva
        if (dto.password() != null && !dto.password().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(dto.password()));
            usuario.setPasswordPlain(dto.password());
        }

        return usuarioRepo.save(usuario);
    }
    public void eliminar(Long id) {
        usuarioRepo.deleteById(id);
    }

    // Nuevo método para obtener clientes activos
    public List<Usuario> listarClientesActivos() {
        return usuarioRepo.findByTipoUsuarioInAndEstado(
                List.of("ESTANDAR", "PREMIUM"),
                "ACTIVO");
    }
}