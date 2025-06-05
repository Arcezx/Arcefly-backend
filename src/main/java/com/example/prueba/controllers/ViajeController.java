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
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

    //APP
    @GetMapping("/api/viajes/busqueda")
    public List<Viaje> buscarViajesEntreFechas(
            @RequestParam String origen,
            @RequestParam String destino,
            @RequestParam String fechaInicio,
            @RequestParam String fechaFin,
            @RequestParam Long idUsuario
    ) {
        List<Viaje> resultados = viajeRepository.buscarPorOrigenDestinoYRangoFechas(origen, destino, fechaInicio, fechaFin);
        List<Long> idsReservados = reservaRepository.findIdsViajesReservadosPorUsuario(idUsuario);

        return resultados.stream()
                .filter(v -> !idsReservados.contains(v.getId()))
                .collect(Collectors.toList());
    }

    @GetMapping("/busqueda-completa")
    public ResponseEntity<?> buscarVuelosExactos(
            @RequestParam String origen,
            @RequestParam String destino,
            @RequestParam String fechaInicio,
            @RequestParam String fechaFin,
            @RequestParam Long idUsuario) {

        List<Viaje> vuelos = viajeService.buscarVuelosExactos(origen, destino, fechaInicio, fechaFin, idUsuario);

        if (!vuelos.isEmpty()) {
            return ResponseEntity.ok(Map.of(
                    "tipo", "exactos",
                    "vuelos", vuelos
            ));
        }

        // 2. Si no hay coincidencias exactas, buscar alternativas
        List<Viaje> alternativos = viajeService.buscarVuelosAlternativos(origen, destino, idUsuario);

        return ResponseEntity.ok(Map.of(
                "tipo", alternativos.isEmpty() ? "ninguno" : "alternativos",
                "vuelos", alternativos,
                "mensaje", alternativos.isEmpty()
                        ? "No hay vuelos disponibles para estas fechas"
                        : "No hay vuelos para las fechas seleccionadas, pero te mostramos otras opciones"
        ));
    }
}
