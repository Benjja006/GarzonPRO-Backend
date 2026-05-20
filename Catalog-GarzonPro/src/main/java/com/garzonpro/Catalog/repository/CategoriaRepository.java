package com.garzonpro.Catalog.repository;

import com.garzonpro.Catalog.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    // JpaRepository ya incluye por defecto:
    // .save(), .findById(), .findAll(), .deleteById(), etc.
}