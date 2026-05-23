CREATE TABLE IF NOT EXISTS mesa (
                                    id_mesa BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    nombre_mesa VARCHAR(50) NOT NULL,
    estado VARCHAR(20) DEFAULT 'LIBRE'
    );

-- Insertamos unas mesas de prueba para la demostración en vivo con el profesor
INSERT INTO mesa (nombre_mesa, estado) VALUES ('Mesa 1', 'LIBRE'), ('Mesa 2', 'LIBRE'), ('Mesa 3', 'LIBRE');