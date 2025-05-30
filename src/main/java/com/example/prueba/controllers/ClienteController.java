package com.example.prueba.controllers;

import com.example.prueba.dtos.requests.CreateClienteRequest;
import com.example.prueba.dtos.responses.ClienteResponse;
import com.example.prueba.dtos.ClienteDTO;
import com.example.prueba.models.Usuario;
import com.example.prueba.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/clientes")
@CrossOrigin(origins = "*")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    public ClienteResponse crearCliente(@RequestBody CreateClienteRequest request) {
        Usuario guardado = clienteService.crearCliente(request);
        return convertirAResponse(guardado);
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponse>> listarClientes() {
        List<Usuario> usuarios = clienteService.listarClientes();

        if(usuarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<ClienteResponse> response = usuarios.stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/buscar")
    public List<ClienteResponse> buscarClientes(@RequestParam String filtro) {
        return clienteService.buscarClientes(filtro).stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}")
    public ClienteResponse actualizarCliente(
            @PathVariable Long id,
            @RequestBody ClienteDTO dto) {
        Usuario actualizado = clienteService.actualizarCliente(id, dto);
        return convertirAResponse(actualizado);
    }

    @DeleteMapping("/{id}")
    public void eliminarCliente(@PathVariable Long id) {
        clienteService.eliminar(id);
    }

    private ClienteResponse convertirAResponse(Usuario usuario) {
        return new ClienteResponse(
                usuario.getIdUsuario(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getPasswordPlain(),
                usuario.getTipoUsuario(),
                usuario.getEstado(), // Nuevo campo estado
                LocalDateTime.now()
        );
    }
}