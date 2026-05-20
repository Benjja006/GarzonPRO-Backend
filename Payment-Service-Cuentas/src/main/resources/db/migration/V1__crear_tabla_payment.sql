CREATE TABLE IF NOT EXISTS pago (
                                    id_pago BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    id_pedido BIGINT,
                                    monto_total DOUBLE,
                                    metodo_pago VARCHAR(255),
    estado_pago VARCHAR(255),
    fecha_pago DATETIME(6)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;