package com.garzonpro.Order.controller;

import com.garzonpro.Order.model.Pedido;
import com.garzonpro.Order.model.DetallePedido;
import com.garzonpro.Order.repository.PedidoRepository;
import com.garzonpro.Order.exception.ResourceNotFoundException;
import com.garzonpro.Order.client.TableClient; // Importamos el cliente de mesas
import com.garzonpro.Order.client.KdsClient;   // Importamos el cliente de cocina
import com.garzonpro.Order.client.dto.TicketCocinaRequestDTO; // Importamos el DTO del ticket
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private PedidoRepository repository;

    @Autowired
    private TableClient tableClient; // Inyectamos Feign Client de Mesas

    @Autowired
    private KdsClient kdsClient;     // Inyectamos Feign Client de Cocina

    // Crear un nuevo pedido vacío para una mesa y cambiar el estado de la mesa
    @PostMapping("/abrir/{idMesa}")
    public Pedido abrirPedido(@PathVariable Long idMesa) {
        Pedido nuevo = new Pedido();
        nuevo.setIdMesa(idMesa);
        Pedido pedidoGuardado = repository.save(nuevo);

        // REQUISITO PAUTA: Comunicación inter-servicio vía Feign Client
        // Notificamos a Table-Service que la mesa ahora está OCUPADA
        tableClient.actualizarEstadoMesa(idMesa, "OCUPADA");

        return pedidoGuardado;
    }

    // Agregar platos a un pedido existente y notificar a la cocina
    @PostMapping("/{idPedido}/agregar")
    public Pedido agregarPlatos(@PathVariable Long idPedido, @RequestBody DetallePedido detalle) {
        Pedido pedido = repository.findById(idPedido)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con ID: " + idPedido));

        pedido.agregarDetalle(detalle);
        Pedido pedidoActualizado = repository.save(pedido);

        // REQUISITO PAUTA: Notificar a la cocina (KDS) enviando el DTO correspondiente
        TicketCocinaRequestDTO ticket = new TicketCocinaRequestDTO();
        ticket.setIdPedido(pedidoActualizado.getIdPedido());
        ticket.setIdMesa(pedidoActualizado.getIdMesa());
        ticket.setNivelAlerta("NORMAL"); // Puede cambiar según lógica de negocio

        kdsClient.notificarNuevoPedido(ticket);

        return pedidoActualizado;
    }

    // Ver el estado de un pedido
    @GetMapping("/{id}")
    public Pedido obtenerPedido(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con ID: " + id));
    }
}