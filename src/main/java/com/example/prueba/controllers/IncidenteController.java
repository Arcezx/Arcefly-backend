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
@CrossOrigin(origins = {"https://arcefly.netlify.app", "http://localhost:4200"})
public class IncidenteController {

    private final EmailService emailService;
    private static final Logger logger = LoggerFactory.getLogger(IncidenteController.class);

    public IncidenteController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping
    public ResponseEntity<?> reportarIncidente(@RequestBody IncidenteRequest request) {
        try {
            // Input validation
            if (request.getArea() == null || request.getArea().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "El área es requerida"
                ));
            }

            logger.info("Recibiendo reporte para área: {}", request.getArea());

            // First validate email configuration works
            emailService.verifyEmailConfiguration();

            // Send email
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
        } catch (EmailService.EmailConfigException e) {
            logger.error("Configuración de correo inválida: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(Map.of(
                    "success", false,
                    "message", "Error de configuración del sistema. Por favor contacte al administrador."
            ));
        } catch (Exception e) {
            logger.error("Error al procesar reporte", e);
            return ResponseEntity.internalServerError().body(Map.of(
                    "success", false,
                    "message", "Error al procesar el reporte. Por favor intente más tarde."
            ));
        }
    }

    private String construirMensaje(IncidenteRequest request) {
        return String.format(
                "Área afectada: %s%n%nDescripción:%n%s%n%nContacto: %s%nFecha: %s",
                request.getArea(),
                request.getDescripcion(),
                request.getEmail(),
                java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
        );
    }
}