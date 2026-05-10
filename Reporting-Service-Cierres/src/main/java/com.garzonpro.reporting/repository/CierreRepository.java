package com.garzonpro.reporting.repository;

import com.garzonpro.reporting.model.CierreCaja;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface CierreRepository extends JpaRepository<CierreCaja, Long> {
    Optional<CierreCaja> findByFecha(LocalDate fecha);
}