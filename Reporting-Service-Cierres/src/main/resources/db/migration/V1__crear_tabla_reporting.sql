CREATE TABLE IF NOT EXISTS cierre_caja (
                                           id_cierre BIGINT AUTO_INCREMENT PRIMARY KEY,
                                           fecha DATE,
                                           total_ventas_dia DOUBLE,
                                           cantidad_pedidos INT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;