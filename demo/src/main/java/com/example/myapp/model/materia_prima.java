package com.example.myapp.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "materia_prima")
public class materia_prima {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_materia_prima")
    private Integer idMateriaPrima;

    private String nombre;
    private String descripcion;
    
    // Cambiado a BigDecimal para coincidir con NUMERIC en PostgreSQL
    private BigDecimal costo;

    // Cambiado a Integer para coincidir con INTEGER en PostgreSQL
    @Column(name = "cantidad_actual")
    private Integer cantidadActual;

    @Column(name = "unidad_medida")
    private String unidadMedida;

    private String estatus;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;

    public materia_prima() {}

    // --- ALIAS Y MÉTODOS DE CONVENIENCIA AGREGADOS ---

    // Permite que las consultas que busquen por .getId() funcionen sin cambiar código
    public Integer getId() { 
        return idMateriaPrima; 
    }

    // Permite que la lectura del campo cantidad_actual funcione si la relación busca getCantidad_actual()
    public Integer getCantidad_actual() { 
        return cantidadActual; 
    }

    // Getters y Setters Estándar
    public Integer getIdMateriaPrima() { return idMateriaPrima; }
    public void setIdMateriaPrima(Integer idMateriaPrima) { this.idMateriaPrima = idMateriaPrima; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public BigDecimal getCosto() { return costo; }
    public void setCosto(BigDecimal costo) { this.costo = costo; }

    public Integer getCantidadActual() { return cantidadActual; }
    public void setCantidadActual(Integer cantidadActual) { this.cantidadActual = cantidadActual; }

    public String getUnidadMedida() { return unidadMedida; }
    public void setUnidadMedida(String unidadMedida) { this.unidadMedida = unidadMedida; }

    public String getEstatus() { return estatus; }
    public void setEstatus(String estatus) { this.estatus = estatus; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
}