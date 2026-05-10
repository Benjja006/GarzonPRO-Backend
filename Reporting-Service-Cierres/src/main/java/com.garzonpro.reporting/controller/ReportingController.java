package com.garzonpro.reporting.controller;

import com.garzonpro.reporting.model.CierreCaja;
import com.garzonpro.reporting.repository.CierreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reporting")
public class ReportingController {

    @Autowired
    private CierreRepository repository;

    @GetMapping("/historial")
    public List<CierreCaja> obtenerHistorial() {
        return repository.findAll();
    }

    @PostMapping("/guardar-cierre")
    public CierreCaja guardarCierre(@RequestBody CierreCaja cierre) {
        return repository.save(cierre);
    }
}