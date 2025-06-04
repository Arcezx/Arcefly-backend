package com.example.prueba.services;

import com.example.prueba.dtos.requests.CreateReservaRequest;
import com.example.prueba.dtos.requests.UpdateReservaRequest;
import com.example.prueba.dtos.responses.ReservaResponse;
import com.example.prueba.models.Reserva;
import com.example.prueba.repositories.ReservaRepository;
import com.example.prueba.repositories.UsuarioRepository;
import com.example.prueba.repositories.ViajeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final ViajeRepository viajeRepository;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public ReservaService(ReservaRepository reservaRepository,
                          ViajeRepository viajeRepository,
                          UsuarioRepository usuarioRepository) {
        this.reservaRepository = reservaRepository;
        this.viajeRepository = viajeRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public List<Reserva> obtenerTodasLasReservas() {
        return reservaRepository.findAllWithViaje();
    }

    @Transactional(readOnly = true)
    public boolean existeReserva(Long idViaje, Long idUsuario) {
        return reservaRepository.existsByIdViajeAndIdUsuario(idViaje, idUsuario);
    }

    @Transactional
    public Reserva crearReserva(CreateReservaRequest request) {
        Reserva reserva = new Reserva();
        reserva.setIdViaje(request.idViaje());
        reserva.setIdUsuario(request.idUsuario());
        reserva.setAsiento(request.asiento());
        reserva.setFechaReserva(LocalDate.now());
        reserva.setEstado("POR CONFIRMAR");
        return reservaRepository.save(reserva);
    }

    @Transactional
    public Reserva actualizarReserva(Long idReserva, UpdateReservaRequest request) {
        Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new EntityNotFoundException("Reserva no encontrada con ID: " + idReserva));

        if (!reserva.getAsiento().equals(request.asiento())) {
            if (!validarDisponibilidadAsiento(request.idViaje(), request.asiento())) {
                throw new IllegalStateException("El asiento " + request.asiento() + " ya está ocupado");
            }
            reserva.setAsiento(request.asiento());
        }

        if (!reserva.getIdViaje().equals(request.idViaje())) {
            if (validarReservaExistente(reserva.getIdUsuario(), request.idViaje())) {
                throw new IllegalStateException("El usuario ya tiene una reserva para este viaje");
            }
            reserva.setIdViaje(request.idViaje());
        }

        reserva.setEstado(request.estado());
        return reservaRepository.save(reserva);
    }

    @Transactional
    public Reserva confirmarReserva(Long idReserva) {
        Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new EntityNotFoundException("Reserva no encontrada"));

        if (!"POR CONFIRMAR".equals(reserva.getEstado())) {
            throw new IllegalStateException("Solo se pueden confirmar reservas en estado POR CONFIRMAR");
        }

        reserva.setEstado("ACTIVA");
        return reservaRepository.save(reserva);
    }

    @Transactional
    public Reserva cancelarReserva(Long idReserva) {
        Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new EntityNotFoundException("Reserva no encontrada"));

        reserva.setEstado("CANCELADA");
        return reservaRepository.save(reserva);
    }

    @Transactional(readOnly = true)
    public List<Reserva> obtenerReservasPorUsuario(Long idUsuario) {
        return reservaRepository.findByIdUsuario(idUsuario);
    }

    public boolean validarDisponibilidadAsiento(Long idViaje, String asiento) {
        return !reservaRepository.existsByIdViajeAndAsiento(idViaje, asiento);
    }

    public boolean validarReservaExistente(Long idUsuario, Long idViaje) {
        return reservaRepository.existsByIdUsuarioAndIdViaje(idUsuario, idViaje);
    }

    @Transactional
    public Reserva crearReservaConValidacion(CreateReservaRequest request) {
        viajeRepository.findById(request.idViaje())
                .orElseThrow(() -> new EntityNotFoundException("Viaje no encontrado"));
        usuarioRepository.findById(request.idUsuario())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        if (!validarDisponibilidadAsiento(request.idViaje(), request.asiento())) {
            throw new IllegalStateException("El asiento " + request.asiento() + " ya está ocupado");
        }

        if (validarReservaExistente(request.idUsuario(), request.idViaje())) {
            throw new IllegalStateException("El usuario ya tiene una reserva para este viaje");
        }

        return crearReserva(request);
    }

    // APP
    public Reserva obtenerReservaActualDeUsuario(Long idUsuario) {
        return reservaRepository.findReservaActualByUsuario(idUsuario)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró una reserva actual"));
    }
}