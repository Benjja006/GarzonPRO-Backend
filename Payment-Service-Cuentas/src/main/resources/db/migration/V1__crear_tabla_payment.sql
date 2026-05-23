CREATE TABLE IF NOT EXISTS pago (
                                    id_pago BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    id_pedido BIGINT NOT NULL,
                                    monto_total DOUBLE NOT NULL,
                                    metodo_pago VARCHAR(50) NOT NULL,
    estado_pago VARCHAR(50) DEFAULT 'COMPLETADO',
    fecha_pago DATETIME NOT NULL
    );