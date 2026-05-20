package com.garzonpro.Table.repository;

import com.garzonpro.Table.model.Mesa;
import com.garzonpro.Table.model.EnumTableStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MesaRepository extends JpaRepository<Mesa, Long> {
    List<Mesa> findByEstado(EnumTableStatus estado);
}