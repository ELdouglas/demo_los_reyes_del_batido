package com.example.myapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ProductoMateriaPrimaId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "id_productos")
    private Integer idProductos;

    @Column(name = "id_materia_prima")
    private Integer idMateriaPrima;

    public ProductoMateriaPrimaId() {}

    public ProductoMateriaPrimaId(Integer idProductos, Integer idMateriaPrima) {
        this.idProductos = idProductos;
        this.idMateriaPrima = idMateriaPrima;
    }

    public Integer getIdProductos() { 
        return idProductos; 
    }
    
    public void setIdProductos(Integer idProductos) { 
        this.idProductos = idProductos; 
    }

    public Integer getIdMateriaPrima() { 
        return idMateriaPrima; 
    }
    
    public void setIdMateriaPrima(Integer idMateriaPrima) { 
        this.idMateriaPrima = idMateriaPrima; 
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductoMateriaPrimaId that = (ProductoMateriaPrimaId) o;
        return Objects.equals(idProductos, that.idProductos) &&
               Objects.equals(idMateriaPrima, that.idMateriaPrima);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProductos, idMateriaPrima);
    }
}