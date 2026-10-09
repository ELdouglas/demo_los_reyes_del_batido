package com.example.myapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "productos_materia_prima")
public class ProductoMateriaPrima {

    @EmbeddedId
    private ProductoMateriaPrimaId id = new ProductoMateriaPrimaId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idProductos")
    @JoinColumn(name = "id_productos")
    @JsonBackReference
    private Producto producto;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("idMateriaPrima")
    @JoinColumn(name = "id_materia_prima")
    @JsonProperty("materiaPrima")
    private materia_prima materiaPrima;

    @Column(name = "cantidad_requerida")
    @JsonProperty("cantidadRequerida")
    private Integer cantidadRequerida;

    @Column(name = "unidades_de_medida")
    @JsonProperty("unidadesDeMedida")
    private String unidadesDeMedida;

    public ProductoMateriaPrima() {}

    // --- MÉTODOS AUXILIARES PARA MAPEADO JSON ---
    @JsonProperty("idMateriaPrima")
    public void setIdMateriaPrimaAux(Integer idMateriaPrima) {
        if (idMateriaPrima != null) {
            if (this.materiaPrima == null) {
                this.materiaPrima = new materia_prima();
            }
            this.materiaPrima.setIdMateriaPrima(idMateriaPrima);
            this.id.setIdMateriaPrima(idMateriaPrima);
        }
    }

    // Getters y Setters
    public ProductoMateriaPrimaId getId() { 
        return id; 
    }
    
    public void setId(ProductoMateriaPrimaId id) { 
        this.id = id; 
    }

    public Producto getProducto() { 
        return producto; 
    }
    
    public void setProducto(Producto producto) { 
        this.producto = producto; 
    }

    public materia_prima getMateriaPrima() { 
        return materiaPrima; 
    }
    
    public void setMateriaPrima(materia_prima materiaPrima) { 
        this.materiaPrima = materiaPrima;
        if (materiaPrima != null && materiaPrima.getIdMateriaPrima() != null) {
            this.id.setIdMateriaPrima(materiaPrima.getIdMateriaPrima());
        }
    }

    public Integer getCantidadRequerida() { 
        return cantidadRequerida; 
    }
    
    public void setCantidadRequerida(Integer cantidadRequerida) { 
        this.cantidadRequerida = cantidadRequerida; 
    }

    public String getUnidadesDeMedida() { 
        return unidadesDeMedida; 
    }
    
    public void setUnidadesDeMedida(String unidadesDeMedida) { 
        this.unidadesDeMedida = unidadesDeMedida; 
    }
}