CREATE TABLE IF NOT EXISTS pedido (
                                      id_pedido BIGINT AUTO_INCREMENT PRIMARY KEY,
                                      id_mesa BIGINT NOT NULL,
                                      estado_general VARCHAR(255) DEFAULT 'ABIERTO',
    total_parcial DOUBLE DEFAULT 0.0
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS detalle_pedido (
                                              id_detalle BIGINT AUTO_INCREMENT PRIMARY KEY,
                                              id_pedido BIGINT,
                                              id_plato BIGINT,
                                              nombre_plato VARCHAR(255),
    cantidad INT,
    precio_unitario_al_momento_venta DOUBLE,
    CONSTRAINT fk_detalle_pedido FOREIGN KEY (id_pedido) REFERENCES pedido(id_pedido) ON DELETE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;