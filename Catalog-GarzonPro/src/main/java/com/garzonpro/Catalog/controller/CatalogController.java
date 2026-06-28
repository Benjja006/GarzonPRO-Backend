package com.garzonpro.Catalog.controller;

import com.garzonpro.Catalog.dto.CategoriaDTO;
import com.garzonpro.Catalog.dto.PlatoDTO;
import com.garzonpro.Catalog.model.Categoria;
import com.garzonpro.Catalog.model.Plato;
import com.garzonpro.Catalog.service.CatalogService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/catalog")
public class CatalogController {

    private final CatalogService service;

    public CatalogController(CatalogService service) {
        this.service = service;
    }
    // LISTAR PLATOS
// Ruta: GET http://localhost:8083/catalog/platos
// API Gateway: GET http://localhost:8080/catalog/platos
// Acción: Obtiene la lista completa de platos
    @GetMapping("/platos")
    public ResponseEntity<List<Plato>> obtenerTodos() {
        return ResponseEntity.ok(service.obtenerTodosLosPlatos());
    }

    // CREAR PLATO
// Ruta: POST http://localhost:8083/catalog/platos
// API Gateway: POST http://localhost:8080/catalog/platos
// Acción: Registra un nuevo plato validando que no esté duplicado
    @PostMapping("/platos")
    public ResponseEntity<Plato> crearPlato(@Valid @RequestBody PlatoDTO dto) {
        Plato nuevoPlato = service.crearPlato(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPlato);
    }

    // MODIFICAR PLATO
// Ruta: PUT http://localhost:8083/catalog/platos/{id}
// API Gateway: PUT http://localhost:8080/catalog/platos/{id}
// Acción: Actualiza nombre, precio o categoría de un plato por su ID
    @PutMapping("/platos/{id}")
    public ResponseEntity<Plato> modificarPlato(@PathVariable Long id, @Valid @RequestBody PlatoDTO dto) {
        Plato platoModificado = service.modificarPlato(id, dto);
        return ResponseEntity.ok(platoModificado);
    }

    // ELIMINAR PLATO
// Ruta: DELETE http://localhost:8083/catalog/platos/{id}
// API Gateway: DELETE http://localhost:8080/catalog/platos/{id}
// Acción: Elimina físicamente un plato por su ID
    @DeleteMapping("/platos/{id}")
    public ResponseEntity<Void> eliminarPlato(@PathVariable Long id) {
        service.eliminarPlato(id);
        return ResponseEntity.noContent().build();
    }

    // LISTAR CATEGORIAS
// Ruta: GET http://localhost:8083/catalog/categorias
// API Gateway: GET http://localhost:8080/catalog/categorias
// Acción: Obtiene la lista completa de categorías
    @GetMapping("/categorias")
    public ResponseEntity<List<Categoria>> obtenerCategorias() {
        return ResponseEntity.ok(service.obtenerCategorias());
    }

    // CREAR CATEGORIA
// Ruta: POST http://localhost:8083/catalog/categorias
// API Gateway: POST http://localhost:8080/catalog/categorias
// Acción: Registra una nueva categoría validando que no esté duplicada
    @PostMapping("/categorias")
    public ResponseEntity<Categoria> crearCategoria(@Valid @RequestBody CategoriaDTO dto) {
        Categoria nueva = service.crearCategoria(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

    // MODIFICAR CATEGORIA
// Ruta: PUT http://localhost:8083/catalog/categorias/{id}
// API Gateway: PUT http://localhost:8080/catalog/categorias/{id}
// Acción: Actualiza el nombre de una categoría por su ID
    @PutMapping("/categorias/{id}")
    public ResponseEntity<Categoria> modificarCategoria(@PathVariable Long id, @Valid @RequestBody CategoriaDTO dto) {
        Categoria categoriaModificada = service.modificarCategoria(id, dto);
        return ResponseEntity.ok(categoriaModificada);
    }

    // ELIMINAR CATEGORIA
// Ruta: DELETE http://localhost:8083/catalog/categorias/{id}
// API Gateway: DELETE http://localhost:8080/catalog/categorias/{id}
// Acción: Elimina una categoría si no tiene platos asociados
    @DeleteMapping("/categorias/{id}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Long id) {
        service.eliminarCategoria(id);
        return ResponseEntity.noContent().build();
    }
}