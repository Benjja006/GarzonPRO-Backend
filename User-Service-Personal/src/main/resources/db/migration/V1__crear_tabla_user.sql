CREATE TABLE usuario (
                         id_usuario BIGINT PRIMARY KEY, -- Sin estrategia AUTO_INCREMENT
                         nombre VARCHAR(255) NOT NULL,
                         apellido VARCHAR(255) NOT NULL,
                         correo VARCHAR(255) NOT NULL UNIQUE,
                         rol VARCHAR(50) NOT NULL
);