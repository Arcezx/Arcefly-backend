package com.example.prueba.repositories;


import com.example.prueba.models.Viaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ViajeRepository extends JpaRepository<Viaje, Long> {
    List<Viaje> findByEstado(String estado);

    @Query("SELECT v FROM Viaje v WHERE LOWER(v.origen) LIKE LOWER(CONCAT('%', :origen, '%')) AND LOWER(v.destino) LIKE LOWER(CONCAT('%', :destino, '%')) AND v.estado = 'activo'")
    List<Viaje> buscarPorOrigenYDestino(@Param("origen") String origen, @Param("destino") String destino);


    @Query("""
    SELECT v FROM Viaje v
    WHERE LOWER(v.origen) = LOWER(:origen)
      AND LOWER(v.destino) = LOWER(:destino)
      AND v.fechaSalida BETWEEN :fechaInicio AND :fechaFin
      AND v.estado = 'ACTIVO'
    ORDER BY v.fechaSalida
""")
    List<Viaje> buscarPorOrigenDestinoYRangoFechas(@Param("origen") String origen,
                                                   @Param("destino") String destino,
                                                   @Param("fechaInicio") LocalDate fechaInicio,
                                                   @Param("fechaFin") LocalDate fechaFin);


    @Query("""
       SELECT v
       FROM Viaje v
       WHERE LOWER(v.origen)  = LOWER(:origen)
         AND LOWER(v.destino) = LOWER(:destino)
         AND v.fechaSalida BETWEEN :fechaInicio AND :fechaFin
         AND v.estado = 'ACTIVO'
       ORDER BY v.fechaSalida
       """)
    List<Viaje> findByOrigenAndDestinoAndFechas(@Param("origen")      String     origen,
                                                @Param("destino")     String     destino,
                                                @Param("fechaInicio") LocalDate  fechaInicio,
                                                @Param("fechaFin")    LocalDate  fechaFin);

    // Búsqueda por origen y destino (sin fechas)
    @Query("SELECT v FROM Viaje v WHERE " +
            "LOWER(v.origen) = LOWER(:origen) AND " +
            "LOWER(v.destino) = LOWER(:destino) " +
            "ORDER BY v.fechaSalida ASC")
    List<Viaje> findByOrigenAndDestino(
            @Param("origen") String origen,
            @Param("destino") String destino);

    @Query("""
       SELECT v FROM Viaje v
       WHERE LOWER(v.destino) = LOWER(:destino)
       AND   v.estado = 'ACTIVO'
       ORDER BY v.fechaSalida
       """)
    List<Viaje> findByDestino(@Param("destino") String destino);

}