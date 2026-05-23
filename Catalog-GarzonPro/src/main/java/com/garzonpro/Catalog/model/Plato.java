package com.garzonpro.Catalog.model;

import jakarta.persistence.*;

@Entity
@Table(name = "plato")
public class Plato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_plato")
    private Long idPlato;

    @Column(name = "nombre_plato", nullable = false)
    private String nombrePlato;

    @Column(nullable = false)
    private Double precio;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;

    public Plato() {}

    // === GETTERS Y SETTERS MANUALES ===
    public Long getIdPlato() { return idPlato; }
    public void setIdPlato(Long idPlato) { this.idPlato = idPlato; }

    public String getNombrePlato() { return nombrePlato; }
    public void setNombrePlato(String nombrePlato) { this.nombrePlato = nombrePlato; }

    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
}