package com.example.prueba.repositories;

import com.example.prueba.models.Reserva;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    @EntityGraph(attributePaths = {"viaje"})
    @Query("SELECT r FROM Reserva r")
    List<Reserva> findAllWithViaje();

    @Query("SELECT r FROM Reserva r LEFT JOIN FETCH r.viaje WHERE r.idReserva = :id")
    Optional<Reserva> findByIdWithViaje(@Param("id") Long id);

    @EntityGraph(attributePaths = {"viaje"})
    List<Reserva> findByIdUsuario(Long idUsuario);

    boolean existsByIdViajeAndAsiento(Long idViaje, String asiento);

    boolean existsByIdUsuarioAndIdViaje(Long idUsuario, Long idViaje);

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END " +
            "FROM Reserva r WHERE r.idViaje = :idViaje AND r.idUsuario = :idUsuario")
    boolean existsByIdViajeAndIdUsuario(@Param("idViaje") Long idViaje,
                                        @Param("idUsuario") Long idUsuario);


    @Query("SELECT r.viaje.id FROM Reserva r WHERE r.usuario.idUsuario = :idUsuario")
    List<Long> findIdsViajesReservadosPorUsuario(@Param("idUsuario") Long idUsuario);


    @Query("SELECT r FROM Reserva r WHERE r.idUsuario = :idUsuario AND r.estado = 'ACTIVO'")
    Optional<Reserva> findReservaActualByUsuario(@Param("idUsuario") Long idUsuario);
}
