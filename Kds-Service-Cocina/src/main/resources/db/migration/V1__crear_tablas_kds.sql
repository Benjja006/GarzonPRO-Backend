CREATE TABLE IF NOT EXISTS ticket_cocina (
                                             id_ticket BIGINT AUTO_INCREMENT PRIMARY KEY,
                                             estado_general VARCHAR(255)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS item_cocina (
                                           id_item_cocina BIGINT AUTO_INCREMENT PRIMARY KEY,
                                           nombre_plato VARCHAR(255),
    cantidad INT,
    estado VARCHAR(255)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;