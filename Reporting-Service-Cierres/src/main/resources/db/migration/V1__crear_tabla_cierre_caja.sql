CREATE TABLE IF NOT EXISTS cierre_caja (
                                           id_cierre BIGINT AUTO_INCREMENT PRIMARY KEY,
                                           fecha DATE NOT NULL,
                                           total_ventas_dia DOUBLE NOT NULL,
                                           cantidad_pedidos INT NOT NULL,
                                           UNIQUE(fecha)
    );