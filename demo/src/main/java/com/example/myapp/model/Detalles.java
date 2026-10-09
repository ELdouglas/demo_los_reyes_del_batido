package com.example.myapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "detalles")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Detalles {

    @EmbeddedId
    private DetallesId id = new DetallesId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idFactura")
    @JoinColumn(name = "id_factura")
    @JsonBackReference
    private Factura factura;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idProductos")
    @JoinColumn(name = "id_productos")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Producto producto;

    @Column(name = "cantidad_vendida")
    private Integer cantidadVendida;

    public Detalles() {}

    public DetallesId getId() { return id; }
    public void setId(DetallesId id) { this.id = id; }

    public Factura getFactura() { return factura; }
    public void setFactura(Factura factura) { this.factura = factura; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    public Integer getCantidadVendida() { return cantidadVendida; }
    public void setCantidadVendida(Integer cantidadVendida) { this.cantidadVendida = cantidadVendida; }
}