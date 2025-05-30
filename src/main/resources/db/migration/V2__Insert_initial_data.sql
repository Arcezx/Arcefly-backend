-- Datos de usuarios
INSERT INTO usuarios (id_usuario, nombre, email, password, tipo_usuario, password_plain, estado, last_login, created_at) VALUES
                                                                                                                             (1, 'admin', 'admin@example.com', '$2a$10$UNEvk3w3ddP4Mrr1DXFJG.yN9OMRTzmBvPnMa3sbdkFCCIH9RNa3O', 'ADMIN', NULL, NULL, NULL, '2025-05-29 00:11:26'),
                                                                                                                             (2, 'Cristian', 'cristian@example.com', '$2a$10$4ZV7VD1tYPdsySwFMkVfyuyeH6pog774tJmVyjRJ9uFlTFz022VAe', 'PREMIUM', 'america123', 'ACTIVO', NULL, '2025-05-29 00:11:26'),
                                                                                                                             (3, 'María', 'maria@example.com', '$2a$10$UNEvk3w3ddP4Mrr1DXFJG.yN9OMRTzmBvPnMa3sbdkFCCIH9RNa3O', 'ESTANDAR', NULL, NULL, NULL, '2025-05-29 00:11:26'),
                                                                                                                             (4, 'Carlos', 'carlos@example.com', '$2a$10$UNEvk3w3ddP4Mrr1DXFJG.yN9OMRTzmBvPnMa3sbdkFCCIH9RNa3O', 'PREMIUM', NULL, NULL, NULL, '2025-05-29 00:11:26'),
                                                                                                                             (5, 'Ricardo', 'prueba@gmail.com', '$2a$10$3rTiwMSNHgIdj2VEAHIG3OAkHo8CWSzVQaFIw86teLgG5XHjLB2M2', 'PREMIUM', 'america123', 'ACTIVO', NULL, '2025-05-29 00:11:26'),
                                                                                                                             (6, 'Milton', 'hola@gmail.com', '$2a$10$Uw1i6d6AL8nidJqjrNRcS..KoUYB00E/2x3DIY2NK1Pcm4GngV9Sy', 'ESTANDAR', 'admin123', 'INACTIVO', NULL, '2025-05-29 00:11:26'),
                                                                                                                             (7, 'Nicol Salazar', 'nicol@example.com', '$2a$10$TvgWTwIToG63kU/4lZd66.WE5XQSkp6bZTTbRKUgriiEoCExuJife', 'ESTANDAR', '130723', 'INACTIVO', NULL, '2025-05-29 00:11:26');

-- Ajustamos las secuencias para que coincidan con los IDs existentes
SELECT setval('usuarios_id_usuario_seq', (SELECT MAX(id_usuario) FROM usuarios));

-- Datos de viajes
INSERT INTO viajes (id_viaje, id_usuario, origen, destino, f_salida, f_regreso, direccion, clase, tipo, capacidad, estado) VALUES
                                                                                                                               (7, 1, 'BARCELONA', 'ROMA', '2025-07-20', '2025-07-27', 'IDA Y VUELTA', 'BUSINESS', 'INTERNACIONAL', 160, 'PROGRAMADO'),
                                                                                                                               (8, 1, 'MALAGA', 'BERLIN', '2025-08-10', '2025-08-20', 'IDA Y VUELTA', 'VIP', 'INTERNACIONAL', 120, 'PROGRAMADO'),
                                                                                                                               (9, 1, 'ZARAGOZA', 'VALENCIA', '2025-09-05', '2025-09-10', 'IDA Y VUELTA', 'ECONOMICA', 'NACIONAL', 200, 'CANCELADO'),
                                                                                                                               (10, 1, 'PALMA', 'IBIZA', '2025-10-15', NULL, 'IDA', 'BUSINESS', 'PROMOCIONAL', 90, 'ATERRIZADO'),
                                                                                                                               (15, 1, 'BARCELONA', 'BERLIN', '2025-05-29', NULL, 'IDA', 'ECONOMICA', 'INTERNACIONAL', 222, 'EN HORA'),
                                                                                                                               (16, 1, 'BERLIN', 'BARCELONA', '2025-05-22', NULL, 'IDA', 'BUSINESS', 'INTERNACIONAL', 222, 'EMBARCANDO'),
                                                                                                                               (18, 1, 'BERLIN', 'PARIS', '2025-05-22', NULL, 'IDA', 'ECONOMICA', 'INTERNACIONAL', 70, 'ATERRIZADO'),
                                                                                                                               (27, 1, 'BARCELONA', 'MADRID', '2025-05-25', NULL, 'IDA', 'BUSINESS', 'INTERNACIONAL', 123, 'ACTIVO'),
                                                                                                                               (28, 1, 'MALAGA', 'GRANADA', '2025-05-31', NULL, 'IDA', 'ECONOMICA', 'NACIONAL', 70, 'EN HORA');

SELECT setval('viajes_id_viaje_seq', (SELECT MAX(id_viaje) FROM viajes));

-- Datos de reservas
INSERT INTO reservas (id_reserva, id_viaje, id_usuario, f_reserva, asiento, estado) VALUES
                                                                                        (7, 7, 4, '2025-06-20', '12F', 'POR CONFIRMAR'),
                                                                                        (9, 9, 3, '2025-08-15', '7H', 'ACTIVA'),
                                                                                        (10, 10, 4, '2025-09-01', '9K', 'CANCELADA'),
                                                                                        (13, 7, 2, '2025-05-28', '12A', 'CANCELADA'),
                                                                                        (14, 7, 3, '2025-05-28', '12B', 'CANCELADA'),
                                                                                        (15, 28, 5, '2025-05-29', '12Z', 'POR CONFIRMAR'),
                                                                                        (16, 28, 6, '2025-05-29', '12c', 'POR CONFIRMAR');

SELECT setval('reservas_id_reserva_seq', (SELECT MAX(id_reserva) FROM reservas));