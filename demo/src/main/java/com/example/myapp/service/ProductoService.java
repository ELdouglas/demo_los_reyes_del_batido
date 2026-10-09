package com.example.myapp.service;

import com.example.myapp.model.Producto;
import com.example.myapp.model.ProductoMateriaPrima;
import com.example.myapp.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> obtenerTodos() {
        return productoRepository.findAll();
    }

    public Optional<Producto> obtenerPorId(Integer id) {
        return productoRepository.findById(id);
    }

    @Transactional
    public Producto guardar(Producto producto) {
        // Asignar estatus por defecto
        if (producto.getEstatus() == null || producto.getEstatus().isEmpty()) {
            producto.setEstatus("ACTIVO");
        }

        // Vincular los items de la receta con el objeto Producto padre
        if (producto.getReceta() != null) {
            for (ProductoMateriaPrima item : producto.getReceta()) {
                item.setProducto(producto);
                
                // Si el producto ya existe (actualización), asociamos su ID al EmbeddableId
                if (producto.getIdProducto() != null) {
                    item.getId().setIdProductos(producto.getIdProducto());
                }
            }
        }

        // Guardar el producto y cascading guardará las filas de productos_materia_prima
        Producto productoGuardado = productoRepository.save(producto);

        // Si era un producto nuevo, asignamos el ID generado por la BD a la clave compuesta de cada receta
        if (productoGuardado.getReceta() != null && !productoGuardado.getReceta().isEmpty()) {
            boolean necesitaReGuardar = false;
            for (ProductoMateriaPrima item : productoGuardado.getReceta()) {
                if (item.getId().getIdProductos() == null) {
                    item.getId().setIdProductos(productoGuardado.getIdProducto());
                    necesitaReGuardar = true;
                }
            }
            if (necesitaReGuardar) {
                productoGuardado = productoRepository.save(productoGuardado);
            }
        }

        return productoGuardado;
    }

    @Transactional
    public boolean eliminar(Integer id) {
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}