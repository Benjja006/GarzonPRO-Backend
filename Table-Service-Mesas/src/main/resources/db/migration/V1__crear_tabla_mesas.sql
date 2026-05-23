CREATE TABLE IF NOT EXISTS configuracion_local (
                                                   id_configuracion BIGINT AUTO_INCREMENT PRIMARY KEY,
                                                   aforo_maximo INT NOT NULL,
                                                   horario_apertura VARCHAR(50),
    horario_cierre VARCHAR(50)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS mesa (
                                    id_mesa BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    nombre_mesa VARCHAR(50) NOT NULL,
    estado VARCHAR(20) DEFAULT 'LIBRE',
    id_configuracion BIGINT,
    CONSTRAINT fk_mesa_config FOREIGN KEY (id_configuracion) REFERENCES configuracion_local(id_configuracion) ON DELETE SET NULL
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;