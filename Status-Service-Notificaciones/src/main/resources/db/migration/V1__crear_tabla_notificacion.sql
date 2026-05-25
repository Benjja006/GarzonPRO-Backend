CREATE TABLE IF NOT EXISTS notificacion (
                                            id_notificacion BIGINT AUTO_INCREMENT PRIMARY KEY,
                                            id_garzon_destino BIGINT NOT NULL,
                                            mensaje VARCHAR(500) NOT NULL,
    leido BOOLEAN DEFAULT FALSE
    );