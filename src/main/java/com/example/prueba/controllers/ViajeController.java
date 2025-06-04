package com.example.prueba.controllers;

import com.example.prueba.dtos.requests.CreateViajeRequest;
import com.example.prueba.dtos.responses.ViajeDropdownResponse;
import com.example.prueba.dtos.responses.ViajeResponse;
import com.example.prueba.models.Viaje;
import com.example.prueba.repositories.ReservaRepository;
import com.example.prueba.repositories.ViajeRepository;
import com.example.prueba.services.ViajeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/viajes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ViajeController {

    private final ViajeService viajeService;
    private final ReservaRepository reservaRepository;
    @Autowired
    private ViajeRepository viajeRepository;

    @PostMapping
    public ResponseEntity<ViajeResponse> crearViaje(@Valid @RequestBody CreateViajeRequest request) {
        return ResponseEntity.ok(viajeService.crearViaje(request));
    }

    @GetMapping
    public ResponseEntity<List<ViajeResponse>> obtenerTodos() {
        return ResponseEntity.ok(viajeService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ViajeResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(viajeService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ViajeResponse> actualizarViaje(
            @PathVariable Long id,
            @Valid @RequestBody CreateViajeRequest request) {
        return ResponseEntity.ok(viajeService.actualizarViaje(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarViaje(@PathVariable Long id) {
        viajeService.eliminarViaje(id);
        return ResponseEntity.noContent().build();
    }

    // -------------------APP------------------
    @GetMapping("/buscar")
    public List<Viaje> buscarViajes(
            @RequestParam String origen,
            @RequestParam String destino,
            @RequestParam Long idUsuario
    ) {
        List<Viaje> viajes = viajeRepository.buscarPorOrigenYDestino(origen, destino);

        List<Long> idsReservados = reservaRepository.findIdsViajesReservadosPorUsuario(idUsuario);
        return viajes.stream()
                .filter(v -> !idsReservados.contains(v.getId()))
                .toList();
    }
    @GetMapping("/sugerencias")
    public Set<String> sugerencias() {
        List<Viaje> viajes = viajeRepository.findAll();
        Set<String> sugerencias = new HashSet<>();
        for (Viaje v : viajes) {
            sugerencias.add(v.getOrigen());
            sugerencias.add(v.getDestino());
        }
        return sugerencias;
    }


}