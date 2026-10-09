package com.example.myapp.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "factura")
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_factura")
    private Integer idFactura;

    @Column(name = "fecha_emision")
    private LocalDateTime fechaEmision;

    @Column(name = "tasa_cambio")
    private BigDecimal tasaCambio;

    @Column(name = "metodo_pago")
    private String metodoPago;

    @Column(name = "estado")
    private String estado;

    @Column(name = "estatus")
    private String estatus;

    @Column(name = "numero_factura", nullable = false)
    private String numeroFactura;

    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Detalles> detalles = new ArrayList<>();

    public Factura() {
        this.fechaEmision = LocalDateTime.now();
    }

    public Integer getIdFactura() { return idFactura; }
    public void setIdFactura(Integer idFactura) { this.idFactura = idFactura; }

    public LocalDateTime getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(LocalDateTime fechaEmision) { this.fechaEmision = fechaEmision; }

    public BigDecimal getTasaCambio() { return tasaCambio; }
    public void setTasaCambio(BigDecimal tasaCambio) { this.tasaCambio = tasaCambio; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getEstatus() { return estatus; }
    public void setEstatus(String estatus) { this.estatus = estatus; }

    public List<Detalles> getDetalles() { return detalles; }
    public void setDetalles(List<Detalles> detalles) { this.detalles = detalles; }

    public String getNumeroFactura() { return numeroFactura; }
    public void setNumeroFactura(String numeroFactura) { this.numeroFactura = numeroFactura; }

    public void agregarDetalle(Detalles detalle) {
        detalles.add(detalle);
        detalle.setFactura(this);
    }
}