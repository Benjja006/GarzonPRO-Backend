CREATE TABLE IF NOT EXISTS ticket_cocina (
                                             id_ticket BIGINT AUTO_INCREMENT PRIMARY KEY,
                                             id_pedido BIGINT NOT NULL,
                                             id_mesa BIGINT NOT NULL,
                                             estado_general VARCHAR(50) DEFAULT 'PENDIENTE',
    hora_llegada DATETIME NOT NULL,
    tiempo_preparacion INT DEFAULT 0,
    nivel_alerta_demora VARCHAR(50) NOT NULL
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS item_cocina (
                                           id_item_cocina BIGINT AUTO_INCREMENT PRIMARY KEY,
                                           id_ticket BIGINT,
                                           nombre_plato VARCHAR(255) NOT NULL,
    cantidad INT NOT NULL,
    estado VARCHAR(50) DEFAULT 'EN_COLA',
    CONSTRAINT fk_item_ticket FOREIGN KEY (id_ticket) REFERENCES ticket_cocina(id_ticket) ON DELETE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;