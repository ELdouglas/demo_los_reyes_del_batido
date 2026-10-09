package com.example.myapp.controller;

import com.example.myapp.model.Categoria;
import com.example.myapp.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(origins = "*")
public class CategoriaController {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @GetMapping
    public ResponseEntity<Map<String, Object>> listarCategorias() {
        List<Categoria> categoriasBD = categoriaRepository.findAll();

        List<Map<String, Object>> listaMap = new ArrayList<>();
        for (Categoria cat : categoriasBD) {
            Map<String, Object> map = new HashMap<>();
            map.put("idCategoria", cat.getIdCategoria());
            map.put("nombre", cat.getNombre());
            map.put("tipoCategoria", cat.getTipoCategoria());
            listaMap.add(map);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("root", listaMap);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<?> crearCategoria(@RequestBody Categoria nuevaCategoria) {
        try {
            Categoria guardada = categoriaRepository.save(nuevaCategoria);
            return ResponseEntity.status(HttpStatus.CREATED).body(guardada);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al guardar en la base de datos: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Integer id) {
        return categoriaRepository.findById(id)
                .map(categoria -> ResponseEntity.ok().body(categoria))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body((Categoria) null));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarCategoria(@PathVariable Integer id, @RequestBody Categoria categoriaDetalles) {
        try {
            return categoriaRepository.findById(id).map(categoriaExistente -> {
                categoriaExistente.setNombre(categoriaDetalles.getNombre());
                categoriaExistente.setTipoCategoria(categoriaDetalles.getTipoCategoria());
                Categoria actualizada = categoriaRepository.save(categoriaExistente);
                return ResponseEntity.ok().body(actualizada);
            }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body((Categoria) null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar la categoría: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCategoria(@PathVariable Integer id) {
        try {
            if (!categoriaRepository.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontró la categoría con ID: " + id);
            }
            categoriaRepository.deleteById(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Categoría eliminada correctamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar la categoría: " + e.getMessage());
        }
    }
}