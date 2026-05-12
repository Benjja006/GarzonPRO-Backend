package com.garzonpro.order.service;

import com.garzonpro.order.model.*;
import com.garzonpro.order.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

@Service
public class OrderService {

    @Autowired
    private PedidoRepository pedidoRepo;

    @Transactional
    public Pedido abrirPedido(Long idMesa) {
        Pedido nuevoPedido = new Pedido();
        nuevoPedido.setIdMesa(idMesa);
        nuevoPedido.setEstadoGeneral("Abierto");
        nuevoPedido.setTotalParcial(0.0);
        return pedidoRepo.save(nuevoPedido);
    }

    @Transactional
    public Pedido agregarItem(Long idPedido, DetallePedido detalle) {
        Pedido pedido = pedidoRepo.findById(idPedido).orElseThrow();

        detalle.setPedido(pedido);
        pedido.getDetalles().add(detalle);

        // Recalcular total
        Double nuevoTotal = pedido.getDetalles().stream()
                .mapToDouble(d -> d.getPrecioUnitarioAlMomentoVenta() * d.getCantidad())
                .sum();
        pedido.setTotalParcial(nuevoTotal);

        return pedidoRepo.save(pedido);
    }
}