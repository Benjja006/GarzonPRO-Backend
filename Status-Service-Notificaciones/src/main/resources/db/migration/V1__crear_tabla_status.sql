CREATE TABLE IF NOT EXISTS notificacion (
                                            id_notificacion BIGINT AUTO_INCREMENT PRIMARY KEY,
                                            id_garzon_destino BIGINT,
                                            mensaje VARCHAR(255),
    leido BOOLEAN NOT NULL DEFAULT FALSE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;