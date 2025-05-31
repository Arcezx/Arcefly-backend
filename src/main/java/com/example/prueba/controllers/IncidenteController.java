package com.example.prueba.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.prueba.dtos.requests.IncidenteRequest;
import com.example.prueba.services.EmailService;

import java.util.Map;

@RestController
@RequestMapping("/api/incidentes")
public class IncidenteController {

    private final EmailService emailService;
    private static final Logger logger = LoggerFactory.getLogger(IncidenteController.class);

    public IncidenteController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping
    public ResponseEntity<?> reportarIncidente(@RequestBody IncidenteRequest request) {
        try {
            logger.info("Intentando enviar reporte para área: {}", request.getArea());

            emailService.enviarEmailDeIncidente(
                    "arceflyy@gmail.com",
                    "Nuevo reporte de incidente - " + request.getArea(),
                    construirMensaje(request)
            );

            logger.info("Correo enviado con éxito");
            return ResponseEntity.ok().body(Map.of(
                    "success", true,
                    "message", "Reporte recibido. Gracias por tu feedback."
            ));
        } catch (Exception e) {
            logger.error("Error al enviar correo", e);
            return ResponseEntity.internalServerError().body(Map.of(
                    "success", false,
                    "message", "Error al enviar el reporte: " + e.getMessage()
            ));
        }
    }

    private String construirMensaje(IncidenteRequest request) {
        return String.format(
                """
                **Área afectada:** %s
                **Descripción del problema:** 
                %s
                
                **Contacto:** %s
                **Fecha:** %s
                """,
                request.getArea(),
                request.getDescripcion(),
                request.getEmail(),
                java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
        );
    }
}