package com.garzonpro.Order.repository;

import com.garzonpro.Order.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByIdMesaAndEstadoGeneral(Long idMesa, String estado);
}