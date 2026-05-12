CREATE TABLE credencial (
                            username VARCHAR(255) PRIMARY KEY,
                            pin_usuario VARCHAR(4) NOT NULL,
                            token_sesion VARCHAR(255),
                            id_usuario BIGINT NOT NULL
);

CREATE TABLE sesion (
                        id_sesion BIGINT AUTO_INCREMENT PRIMARY KEY,
                        id_usuario BIGINT NOT NULL,
                        fecha_inicio DATETIME,
                        fecha_fin DATETIME,
                        rol_usuario VARCHAR(50)
);