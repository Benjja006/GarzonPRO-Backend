package com.garzonpro.Catalog.repository;

import com.garzonpro.Catalog.model.Plato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatoRepository extends JpaRepository<Plato, Long> {
    // Verifica si ya existe un plato con ese nombre exacto
    boolean existsByNombrePlato(String nombrePlato);

    // Verifica si hay platos asociados a una categoría específica (para el borrado seguro)
    boolean existsByCategoriaIdCategoria(Long idCategoria);
}