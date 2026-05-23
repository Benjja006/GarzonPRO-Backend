CREATE TABLE IF NOT EXISTS categoria (
                                         id_categoria BIGINT AUTO_INCREMENT PRIMARY KEY,
                                         nombre_categoria VARCHAR(255) NOT NULL
    );

CREATE TABLE IF NOT EXISTS plato (
                                     id_plato BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     nombre_plato VARCHAR(255) NOT NULL,
    precio DOUBLE NOT NULL,
    id_categoria BIGINT NOT NULL,
    CONSTRAINT fk_plato_categoria FOREIGN KEY (id_categoria) REFERENCES categoria(id_categoria)
    );

-- Opcional: Insertar unas categorías base para que la app funcione al tiro en la demo
INSERT INTO categoria (nombre_categoria) VALUES ('Entradas'), ('Fondos'), ('Bebidas');