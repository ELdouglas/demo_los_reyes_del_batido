package com.example.myapp.controller;

import com.example.myapp.model.Categoria;
import com.example.myapp.model.Producto;
import com.example.myapp.model.ProductoMateriaPrima;
import com.example.myapp.repository.CategoriaRepository;
import com.example.myapp.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    // 1. OBTENER LISTA (Para consultar desde Codename One y el Navegador)
    @GetMapping
    public ResponseEntity<List<Producto>> obtenerProductos() {
        List<Producto> lista = productoRepository.findAll();
        return ResponseEntity.ok(lista);
    }

    // 2. CREAR PRODUCTO (Para guardar nuevos productos)
    @PostMapping
    public ResponseEntity<?> crearProducto(@RequestBody Producto producto) {
        try {
            // Asigna la referencia del producto padre a cada ítem de la receta
            if (producto.getReceta() != null) {
                for (ProductoMateriaPrima item : producto.getReceta()) {
                    item.setProducto(producto);
                }
            }
            Producto nuevoProducto = productoRepository.save(producto);
            return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace(); // Imprime el stacktrace completo en la consola de Spring Boot
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al guardar el producto: " + e.getMessage());
        }
    }

    // 3. ASIGNAR CATEGORÍA A PRODUCTO EXISTENTE
    @PutMapping("/{idProducto}/categoria/{idCategoria}")
    public ResponseEntity<?> asignarCategoriaAProducto(
            @PathVariable Integer idProducto,
            @PathVariable Integer idCategoria) {
        try {
            Optional<Producto> optProducto = productoRepository.findById(idProducto);
            Optional<Categoria> optCategoria = categoriaRepository.findById(idCategoria);

            if (optProducto.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El producto no existe.");
            }
            if (optCategoria.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La categoría no existe.");
            }

            Producto producto = optProducto.get();
            producto.setIdCategoria(idCategoria); 
            
            productoRepository.save(producto);

            return ResponseEntity.ok().body("Categoría asignada correctamente.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al asignar categoría: " + e.getMessage());
        }
    }
    
    // 4. CAMBIAR ESTADO DE UN PRODUCTO (Activo / Inactivo)
    @PutMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(
            @PathVariable Integer id,
            @RequestParam String estatus) {
        try {
            Optional<Producto> optProducto = productoRepository.findById(id);
            if (optProducto.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El producto no existe.");
            }

            Producto producto = optProducto.get();
            producto.setEstatus(estatus);
            productoRepository.save(producto);

            return ResponseEntity.ok().body("Estado actualizado a " + estatus);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al cambiar estado: " + e.getMessage());
        }
    }

    // 5. ELIMINAR PRODUCTO (Borrado Físico)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarProducto(@PathVariable Integer id) {
        try {
            Optional<Producto> optProducto = productoRepository.findById(id);
            if (optProducto.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El producto no existe.");
            }

            productoRepository.deleteById(id);
            return ResponseEntity.ok().body("Producto eliminado con éxito.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar producto: " + e.getMessage());
        }
    }
}