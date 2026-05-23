package com.garzonpro.Payment.repository;

import com.garzonpro.Payment.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {

    // Busca todos los pagos asociados a un pedido específico
    List<Pago> findByIdPedido(Long idPedido);
}