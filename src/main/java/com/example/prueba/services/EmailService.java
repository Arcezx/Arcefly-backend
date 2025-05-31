package com.example.prueba.services;

import jakarta.mail.MessagingException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void enviarEmailDeIncidente(String destinatario, String asunto, String mensaje) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(destinatario);
        email.setSubject(asunto);
        email.setText(mensaje);
        mailSender.send(email);
    }
    public void verifyEmailConfiguration() throws EmailConfigException {
        try {
            JavaMailSenderImpl mailSender = (JavaMailSenderImpl) this.mailSender;
            mailSender.testConnection();
        } catch (MessagingException e) {
            throw new EmailConfigException("Mail server configuration error: " + e.getMessage());
        }
    }

    public static class EmailConfigException extends Exception {
        public EmailConfigException(String message) {
            super(message);
        }
    }
}