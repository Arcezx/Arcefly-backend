package com.example.prueba.controllers;

import com.example.prueba.dtos.requests.CreateReservaRequest;
import com.example.prueba.dtos.requests.UpdateReservaRequest;
import com.example.prueba.dtos.responses.ClienteDropdownResponse;
import com.example.prueba.dtos.responses.ReservaResponse;
import com.example.prueba.dtos.responses.ViajeDropdownResponse;
import com.example.prueba.dtos.responses.ViajeResponse;
import com.example.prueba.models.Reserva;
import com.example.prueba.models.Usuario;
import com.example.prueba.services.ClienteService;
import com.example.prueba.services.ReservaService;
import com.example.prueba.services.ViajeService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reservas")
@CrossOrigin(origins = "*")
public class ReservaController {

    private final ReservaService reservaService;
    private final ViajeService viajeService;
    private final ClienteService clienteService;

    @Autowired
    public ReservaController(ReservaService reservaService, ViajeService viajeService,
                             ClienteService clienteService) {
        this.reservaService = reservaService;
        this.viajeService = viajeService;
        this.clienteService = clienteService;
    }

    @GetMapping("/existe")
    public ResponseEntity<Boolean> existeReserva(
            @RequestParam Long idViaje,
            @RequestParam Long idUsuario) {
        boolean existe = reservaService.existeReserva(idViaje, idUsuario);
        return ResponseEntity.ok(existe);
    }

    @PostMapping
    public ResponseEntity<?> crearReserva(@RequestBody CreateReservaRequest request) {
        try {
            Reserva reservaCreada = reservaService.crearReservaConValidacion(request);
            return ResponseEntity.ok(toResponse(reservaCreada));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarReserva(
            @PathVariable Long id,
            @RequestBody UpdateReservaRequest request) {
        try {
            Reserva reservaActualizada = reservaService.actualizarReserva(id, request);
            return ResponseEntity.ok(toResponse(reservaActualizada));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<ReservaResponse>> listarTodasLasReservas() {
        List<Reserva> reservas = reservaService.obtenerTodasLasReservas();
        return ResponseEntity.ok(toResponseList(reservas));
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<ReservaResponse>> listarPorUsuario(@PathVariable Long idUsuario) {
        List<Reserva> reservas = reservaService.obtenerReservasPorUsuario(idUsuario);
        return ResponseEntity.ok(toResponseList(reservas));
    }

    @PatchMapping("/{id}/confirmar")
    @CrossOrigin(methods = {RequestMethod.PATCH, RequestMethod.OPTIONS})
    public ResponseEntity<ReservaResponse> confirmarReserva(@PathVariable Long id) {
        Reserva reservaConfirmada = reservaService.confirmarReserva(id);
        return ResponseEntity.ok(toResponse(reservaConfirmada));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<ReservaResponse> cancelarReserva(@PathVariable Long id) {
        Reserva reservaCancelada = reservaService.cancelarReserva(id);
        return ResponseEntity.ok(toResponse(reservaCancelada));
    }

    @GetMapping("/validar-asiento")
    public ResponseEntity<Boolean> validarAsiento(
            @RequestParam Long idViaje,
            @RequestParam String asiento) {
        boolean disponible = reservaService.validarDisponibilidadAsiento(idViaje, asiento);
        return ResponseEntity.ok(disponible);
    }

    @GetMapping("/clientes-activos")
    public ResponseEntity<List<ClienteDropdownResponse>> listarClientesParaDropdown() {
        List<Usuario> clientes = clienteService.listarClientesActivos();
        List<ClienteDropdownResponse> response = clientes.stream()
                .map(c -> new ClienteDropdownResponse(c.getIdUsuario(), c.getNombre(), c.getEmail()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/viajes-activos")
    public ResponseEntity<List<ViajeDropdownResponse>> listarViajesParaDropdown() {
        List<ViajeResponse> viajes = viajeService.obtenerViajesActivos();
        List<ViajeDropdownResponse> response = viajes.stream()
                .map(v -> new ViajeDropdownResponse(
                        v.getIdViaje(),
                        v.getOrigen() + " - " + v.getDestino(),
                        v.getFechaSalida()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    private ReservaResponse toResponse(Reserva reserva) {
        return new ReservaResponse(
                reserva.getIdReserva(),
                reserva.getIdViaje(),
                reserva.getIdUsuario(),
                reserva.getFechaReserva(),
                reserva.getAsiento(),
                reserva.getEstado(),
                reserva.getViaje() != null ? reserva.getViaje().getOrigen() : "N/A",
                reserva.getViaje() != null ? reserva.getViaje().getDestino() : "N/A"
        );
    }

    private List<ReservaResponse> toResponseList(List<Reserva> reservas) {
        return reservas.stream().map(this::toResponse).collect(Collectors.toList());
    }


    // -------------------APP------------------

    @GetMapping("/usuario/actual")
    public ResponseEntity<?> obtenerReservaActual(@RequestParam Long idUsuario) {

        if (idUsuario == null || idUsuario <= 0) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message","idUsuario inválido"));
        }

        return reservaService.obtenerReservaActualDeUsuario(idUsuario)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }



}
