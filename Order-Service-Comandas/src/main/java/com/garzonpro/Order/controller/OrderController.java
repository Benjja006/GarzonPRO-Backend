package com.garzonpro.Order.controller;

import com.garzonpro.Order.model.Pedido;
import com.garzonpro.Order.model.DetallePedido;
import com.garzonpro.Order.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private PedidoRepository repository;

    // Crear un nuevo pedido vacío para una mesa
    @PostMapping("/abrir/{idMesa}")
    public Pedido abrirPedido(@PathVariable Long idMesa) {
        Pedido nuevo = new Pedido();
        nuevo.setIdMesa(idMesa);
        return repository.save(nuevo);
    }

    // Agregar platos a un pedido existente
    @PostMapping("/{idPedido}/agregar")
    public Pedido agregarPlatos(@PathVariable Long idPedido, @RequestBody DetallePedido detalle) {
        Pedido pedido = repository.findById(idPedido).orElseThrow();
        pedido.agregarDetalle(detalle);
        return repository.save(pedido);
    }

    // Ver el estado de un pedido
    @GetMapping("/{id}")
    public Pedido obtenerPedido(@PathVariable Long id) {
        return repository.findById(id).orElseThrow();
    }
}