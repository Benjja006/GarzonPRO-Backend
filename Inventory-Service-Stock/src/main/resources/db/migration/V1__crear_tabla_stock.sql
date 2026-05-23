CREATE TABLE IF NOT EXISTS stock_plato (
                                           id_stock BIGINT AUTO_INCREMENT PRIMARY KEY,
                                           id_plato BIGINT NOT NULL,
                                           disponible_para_venta BOOLEAN DEFAULT TRUE,
                                           cantidad_restante INT NOT NULL
);