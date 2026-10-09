package com.example.myapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class DetallesId implements Serializable {

    @Column(name = "id_productos")
    private Integer idProductos;

    @Column(name = "id_factura")
    private Integer idFactura;

    public DetallesId() {}

    public DetallesId(Integer idProductos, Integer idFactura) {
        this.idProductos = idProductos;
        this.idFactura = idFactura;
    }

    public Integer getIdProductos() { return idProductos; }
    public void setIdProductos(Integer idProductos) { this.idProductos = idProductos; }

    public Integer getIdFactura() { return idFactura; }
    public void setIdFactura(Integer idFactura) { this.idFactura = idFactura; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DetallesId that = (DetallesId) o;
        return Objects.equals(idProductos, that.idProductos) && Objects.equals(idFactura, that.idFactura);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProductos, idFactura);
    }
}