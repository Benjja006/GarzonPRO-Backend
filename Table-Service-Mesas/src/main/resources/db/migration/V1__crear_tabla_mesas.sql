CREATE TABLE mesa (
                      id_mesa BIGINT AUTO_INCREMENT PRIMARY KEY,
                      nombre_mesa VARCHAR(50) NOT NULL,
                      estado VARCHAR(20) DEFAULT 'LIBRE'
);