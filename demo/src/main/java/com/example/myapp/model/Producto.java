package com.example.myapp.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_productos")
    private Integer idProducto;

    private String nombre;
    private String descripcion;
 
    @Column(name = "sabor", nullable = true) // Asegura que permita valores nulos
private String sabor;

    @Column(name = "precio_venta")
    private BigDecimal precio;

    private String estatus;

    // --- Relación con Categoría ---
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @JsonProperty("receta")
    private List<ProductoMateriaPrima> receta = new ArrayList<>();

    // --- GETTERS Y SETTERS ---
    public Integer getIdProducto() { return idProducto; }
    public void setIdProducto(Integer idProducto) { this.idProducto = idProducto; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getSabor() { return sabor; }
    public void setSabor(String sabor) { this.sabor = sabor; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    public String getEstatus() { return estatus; }
    public void setEstatus(String estatus) { this.estatus = estatus; }

    // GETTER Y SETTER DE CATEGORÍA
    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    public void setIdCategoria(Integer idCategoria) {
        if (idCategoria != null) {
            Categoria cat = new Categoria();
            cat.setIdCategoria(idCategoria);
            this.categoria = cat;
        } else {
            this.categoria = null;
        }
    }

    public List<ProductoMateriaPrima> getReceta() { return receta; }
    
    // ALIAS AGREGADO: Permite llamar a getRecetas() desde FacturaService sin errores de compilación
    public List<ProductoMateriaPrima> getRecetas() { return receta; }

    public void setReceta(List<ProductoMateriaPrima> receta) {
        this.receta = receta;
        if (receta != null) {
            for (ProductoMateriaPrima item : receta) {
                item.setProducto(this);
                if (this.idProducto != null && item.getId() != null) {
                    item.getId().setIdProductos(this.idProducto);
                }
            }
        }
    }

    // MÉTODO AUXILIAR: Para añadir un ítem a la receta garantizando la referencia inversa
    public void agregarIngrediente(ProductoMateriaPrima item) {
        if (this.receta == null) {
            this.receta = new ArrayList<>();
        }
        this.receta.add(item);
        item.setProducto(this);
    }
}