package com.garzonpro.payment.repository;

import com.garzonpro.payment.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {
    // Puedes agregar métodos para buscar pagos por pedido si lo necesitas
}