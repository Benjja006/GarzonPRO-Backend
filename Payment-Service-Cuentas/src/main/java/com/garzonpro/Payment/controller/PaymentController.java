package com.garzonpro.Payment.controller;

import com.garzonpro.Payment.model.Pago;
import com.garzonpro.Payment.repository.PagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PagoRepository repository;

    // Registrar un nuevo pago
    @PostMapping("/procesar")
    public Pago procesarPago(@RequestBody Pago pago) {
        return repository.save(pago);
    }

    // Ver historial de pagos
    @GetMapping("/historial")
    public List<Pago> obtenerTodos() {
        return repository.findAll();
    }

    // Obtener un pago específico por ID
    @GetMapping("/{id}")
    public Pago obtenerPago(@PathVariable Long id) {
        return repository.findById(id).orElseThrow();
    }
}