package com.garzonpro.Order.service;

import com.garzonpro.Order.model.*;
import com.garzonpro.Order.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.math.BigDecimal; // Importante adicionar este import

@Service
public class OrderService {

    @Autowired
    private PedidoRepository pedidoRepo;

    @Transactional
    public Pedido abrirPedido(Long idMesa) {
        Pedido nuevoPedido = new Pedido();
        nuevoPedido.setIdMesa(idMesa);
        nuevoPedido.setEstadoGeneral("Abierto");

        // Se na sua entidade Pedido o 'totalParcial' também mudou para BigDecimal:
        nuevoPedido.setTotalParcial(BigDecimal.ZERO);

        return pedidoRepo.save(nuevoPedido);
    }

    @Transactional
    public Pedido agregarItem(Long idPedido, DetallePedido detalle) {
        Pedido pedido = pedidoRepo.findById(idPedido).orElseThrow();

        detalle.setPedido(pedido);
        pedido.getDetalles().add(detalle);

        // Recalcular total com BigDecimal usando map e reduce
        BigDecimal nuevoTotal = pedido.getDetalles().stream()
                .map(d -> d.getPrecioUnitarioAlMomentoVenta()
                        .multiply(BigDecimal.valueOf(d.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Certifique-se de que o método setTotalParcial na sua classe Pedido
        // agora aceite um parâmetro do tipo BigDecimal
        pedido.setTotalParcial(nuevoTotal);

        return pedidoRepo.save(pedido);
    }
}