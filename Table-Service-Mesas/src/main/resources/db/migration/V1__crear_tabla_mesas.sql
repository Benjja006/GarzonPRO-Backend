CREATE TABLE IF NOT EXISTS mesa (
                                    id_mesa BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    nombre_mesa VARCHAR(50) NOT NULL,
    estado ENUM('LIBRE', 'OCUPADA', 'RESERVADA') NOT NULL DEFAULT 'LIBRE'
    );

-- Insertamos las mesas de prueba con las mayúsculas correspondientes
INSERT INTO mesa (nombre_mesa, estado) VALUES
                                           ('Mesa 1', 'LIBRE'),
                                           ('Mesa 2', 'LIBRE'),
                                           ('Mesa 3', 'LIBRE');