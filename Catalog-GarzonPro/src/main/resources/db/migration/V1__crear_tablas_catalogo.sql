CREATE TABLE categoria (
                           id_categoria BIGINT AUTO_INCREMENT PRIMARY KEY,
                           nombre_categoria VARCHAR(100) NOT NULL
);

CREATE TABLE plato (
                       id_plato BIGINT AUTO_INCREMENT PRIMARY KEY,
                       nombre_plato VARCHAR(255) NOT NULL,
                       precio DOUBLE NOT NULL,
                       id_categoria BIGINT,
                       FOREIGN KEY (id_categoria) REFERENCES categoria(id_categoria)
);