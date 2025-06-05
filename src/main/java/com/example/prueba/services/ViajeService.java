package com.example.prueba.services;

import com.example.prueba.dtos.requests.CreateViajeRequest;
import com.example.prueba.dtos.responses.ViajeDropdownResponse;
import com.example.prueba.dtos.responses.ViajeResponse;
import com.example.prueba.models.Viaje;
import com.example.prueba.repositories.ReservaRepository;
import com.example.prueba.repositories.ViajeRepository;
import com.example.prueba.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
public class ViajeService {

    private final ViajeRepository viajeRepository;
    private final UsuarioRepository usuarioRepository;
    private final ReservaRepository reservaRepository;


    public ViajeService(ViajeRepository viajeRepository, UsuarioRepository usuarioRepository, ReservaRepository reservaRepository) {
        this.viajeRepository = viajeRepository;
        this.usuarioRepository = usuarioRepository;
        this.reservaRepository = reservaRepository;
    }

    @Transactional
    public ViajeResponse crearViaje(CreateViajeRequest request) {
        if (request.getOrigen().equalsIgnoreCase(request.getDestino())) {
            throw new IllegalArgumentException("Origen y destino no pueden ser iguales");
        }

        if (request.getFechaRegreso() != null && request.getFechaRegreso().isBefore(request.getFechaSalida())) {
            throw new IllegalArgumentException("La fecha de regreso debe ser posterior a la de salida");
        }

        Viaje viaje = new Viaje();
        viaje.setUsuario(usuarioRepository.findById(request.getIdUsuario()).orElseThrow());
        viaje.setOrigen(request.getOrigen());
        viaje.setDestino(request.getDestino());
        viaje.setFechaSalida(request.getFechaSalida());
        viaje.setFechaRegreso(request.getFechaRegreso());
        viaje.setCapacidad(request.getCapacidad());
        viaje.setEstado(request.getEstado().toUpperCase());
        viaje.setDireccion(request.getDireccion());
        viaje.setClase(request.getClase());
        viaje.setTipo(request.getTipo());

        viaje = viajeRepository.save(viaje);
        return convertirAViajeResponse(viaje);
    }

    public List<ViajeResponse> obtenerTodos() {
        return viajeRepository.findAll().stream()
                .map(this::convertirAViajeResponse)
                .collect(Collectors.toList());
    }

    public ViajeResponse obtenerPorId(Long id) {
        Viaje viaje = viajeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Viaje no encontrado"));
        return convertirAViajeResponse(viaje);
    }

    @Transactional
    public ViajeResponse actualizarViaje(Long id, CreateViajeRequest request) {
        Viaje viaje = viajeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Viaje no encontrado"));

        if (request.getOrigen().equalsIgnoreCase(request.getDestino())) {
            throw new IllegalArgumentException("Origen y destino no pueden ser iguales");
        }
        if (request.getFechaRegreso() != null && request.getFechaRegreso().isBefore(request.getFechaSalida())) {
            throw new IllegalArgumentException("La fecha de regreso debe ser posterior a la de salida");
        }

        viaje.setOrigen(request.getOrigen());
        viaje.setDestino(request.getDestino());
        viaje.setFechaSalida(request.getFechaSalida());
        viaje.setFechaRegreso(request.getFechaRegreso());
        viaje.setCapacidad(request.getCapacidad());
        viaje.setEstado(request.getEstado().toUpperCase());
        viaje.setDireccion(request.getDireccion());
        viaje.setClase(request.getClase());
        viaje.setTipo(request.getTipo());

        viaje = viajeRepository.save(viaje);
        return convertirAViajeResponse(viaje);
    }

    public void eliminarViaje(Long id) {
        viajeRepository.deleteById(id);
    }

    private ViajeResponse convertirAViajeResponse(Viaje viaje) {
        ViajeResponse response = new ViajeResponse();
        response.setIdViaje(viaje.getId());
        response.setIdUsuario(viaje.getUsuario().getIdUsuario());
        response.setOrigen(viaje.getOrigen());
        response.setDestino(viaje.getDestino());
        response.setFechaSalida(viaje.getFechaSalida());
        response.setFechaRegreso(viaje.getFechaRegreso());
        response.setDireccion(viaje.getDireccion());
        response.setClase(viaje.getClase());
        response.setTipo(viaje.getTipo());
        response.setCapacidad(viaje.getCapacidad());
        response.setEstado(viaje.getEstado());
        response.setEsSoloIda(viaje.getFechaRegreso() == null);
        response.setNombreUsuario(viaje.getUsuario().getNombre());
        return response;
    }

    public List<ViajeResponse> obtenerViajesActivos() {
        return viajeRepository.findByEstado("ACTIVO").stream()
                .map(this::convertirAViajeResponse)
                .collect(Collectors.toList());
    }

    // APP

    public List<Viaje> buscarVuelosExactos(String origen, String destino,
                                           String fechaInicio, String fechaFin,
                                           Long idUsuario) {
        List<Viaje> vuelos = viajeRepository.buscarPorOrigenDestinoYRangoFechas(origen, destino, fechaInicio, fechaFin);
        List<Long> idsReservados = reservaRepository.findViajesReservadosPorUsuario(idUsuario);

        return vuelos.stream()
                .filter(v -> !idsReservados.contains(v.getId()))
                .collect(Collectors.toList());
    }


    public List<Viaje> buscarVuelosAlternativos(String origen, String destino, Long idUsuario) {
        List<Viaje> vuelos = viajeRepository.findByOrigenAndDestino(origen, destino);
        List<Long> idsReservados = reservaRepository.findViajesReservadosPorUsuario(idUsuario);

        return vuelos.stream()
                .filter(v -> !idsReservados.contains(v.getId()))
                .sorted(Comparator.comparing(Viaje::getFechaSalida))
                .limit(5)
                .collect(Collectors.toList());
    }

    public List<Viaje> findByOrigenAndDestinoAndFechas(String origen, String destino,
                                                       String fechaInicio, String fechaFin) {
        return viajeRepository.buscarPorOrigenDestinoYRangoFechas(origen, destino, fechaInicio, fechaFin);
    }

    public List<Viaje> findByOrigenAndDestino(String origen, String destino) {
        return viajeRepository.findByOrigenAndDestino(origen, destino);
    }


}