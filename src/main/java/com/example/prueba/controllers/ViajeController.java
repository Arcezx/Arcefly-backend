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
import java.util.*;
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
    @GetMapping("/busqueda")
    public List<Viaje> buscarViajesEntreFechas(
            @RequestParam String     origen,
            @RequestParam String     destino,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            @RequestParam Long       idUsuario) {

        List<Viaje> brutos = viajeRepository
                .findByOrigenAndDestinoAndFechas(origen, destino, fechaInicio, fechaFin);

        List<Long> idsReservados = reservaRepository.findIdsViajesReservadosPorUsuario(idUsuario);

        return brutos.stream()
                .filter(v -> !idsReservados.contains(v.getId()))
                .toList();
    }



    @GetMapping("/busqueda-completa")
    public ResponseEntity<?> buscarVuelosCompletos(
            @RequestParam String origen,
            @RequestParam String destino,
            @RequestParam String fechaInicio,
            @RequestParam String fechaFin,
            @RequestParam Long idUsuario) {

        try {
            if (origen == null || origen.isBlank() || destino == null || destino.isBlank()) {
                return ResponseEntity.badRequest().body(Map.of(
                        "error", "Origen y destino son requeridos"
                ));
            }

            origen = origen.trim().toLowerCase();
            destino = destino.trim().toLowerCase();

            LocalDate fechaInicioDate;
            LocalDate fechaFinDate;

            try {
                fechaInicioDate = LocalDate.parse(fechaInicio.split("T")[0]);
                fechaFinDate = LocalDate.parse(fechaFin.split("T")[0]);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(Map.of(
                        "error", "Formato de fecha inválido. Use YYYY-MM-DD"
                ));
            }

            List<Viaje> vuelosExactos = Optional.ofNullable(
                    viajeService.findByOrigenAndDestinoAndFechas(
                            origen,
                            destino,
                            fechaInicioDate,
                            fechaFinDate
                    )
            ).orElse(new ArrayList<>());

            List<Long> idsReservados = Optional.ofNullable(
                    reservaRepository.findViajesReservadosPorUsuario(idUsuario)
            ).orElse(new ArrayList<>());

            List<Viaje> disponibles = vuelosExactos.stream()
                    .filter(v -> !idsReservados.contains(v.getId()))
                    .collect(Collectors.toList());

            if (!disponibles.isEmpty()) {
                return ResponseEntity.ok(Map.of(
                        "tipo", "exactos",
                        "vuelos", disponibles,
                        "mensaje", String.format("%d vuelos encontrados", disponibles.size())
                ));
            }

            List<Viaje> alternativos = viajeService.findByOrigenAndDestino(origen, destino)
                    .stream()
                    .filter(v -> !idsReservados.contains(v.getId()))
                    .sorted(Comparator.comparing(Viaje::getFechaSalida))
                    .limit(5)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(Map.of(
                    "tipo", alternativos.isEmpty() ? "ninguno" : "alternativos",
                    "vuelos", alternativos,
                    "mensaje", alternativos.isEmpty()
                            ? "No hay vuelos disponibles para estos criterios"
                            : "No encontramos vuelos para esas fechas, pero tenemos otras opciones"
            ));

        } catch (Exception e) {
            System.out.println("❌ ERROR inesperado en /busqueda-completa:");
            System.out.println("Origen: " + origen);
            System.out.println("Destino: " + destino);
            System.out.println("FechaInicio: " + fechaInicio);
            System.out.println("FechaFin: " + fechaFin);
            System.out.println("ID Usuario: " + idUsuario);
            e.printStackTrace();

            return ResponseEntity.internalServerError().body(Map.of(
                    "error", "Error al procesar la búsqueda",
                    "detalle", e.getMessage()
            ));
        }
    }

    @GetMapping("/por-destino")
    public ResponseEntity<List<Viaje>> buscarSoloPorDestino(
            @RequestParam String destino,
            @RequestParam Long idUsuario) {

        System.out.println("🔍 Buscando vuelos a destino: " + destino);
        System.out.println("👤 ID Usuario: " + idUsuario);

        List<Viaje> vuelos = viajeService.buscarSoloPorDestino(destino);
        System.out.println("✈️ Vuelos encontrados (crudos): " + vuelos.size());

        List<Long> reservados = reservaRepository.findViajesReservadosPorUsuario(idUsuario);
        System.out.println("📌 IDs de viajes reservados: " + reservados);

        vuelos = vuelos.stream()
                .filter(v -> !reservados.contains(v.getId()))
                .collect(Collectors.toList());

        System.out.println("✅ Vuelos disponibles: " + vuelos.size());
        return ResponseEntity.ok(vuelos);
    }


}
