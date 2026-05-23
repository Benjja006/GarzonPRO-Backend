CREATE TABLE configuracion_local (
                                     id_configuracion BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     nombre_parametro VARCHAR(255) NOT NULL,
                                     valor_parametro VARCHAR(255),
                                     aforo_maximo INT,
                                     horario_apertura VARCHAR(255),
                                     horario_cierre VARCHAR(255)
);