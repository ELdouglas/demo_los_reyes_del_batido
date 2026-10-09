package com.example.myapp.controller;

import com.example.myapp.model.materia_prima;
import com.example.myapp.service.MateriaPrimaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/materia-prima")
@CrossOrigin(origins = "*")
public class MateriaPrimaController {

    @Autowired
    private MateriaPrimaService materiaPrimaService;

    @GetMapping
    public ResponseEntity<List<materia_prima>> listar() {
        return ResponseEntity.ok(materiaPrimaService.listarTodas());
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Map<String, Object> payload) {
        try {
            materia_prima mp = new materia_prima();
            mp.setNombre((String) payload.get("nombre"));
            mp.setDescripcion((String) payload.get("descripcion"));
            mp.setUnidadMedida((String) payload.get("unidadMedida"));
            mp.setEstatus(payload.get("estatus") != null ? payload.get("estatus").toString() : "ACTIVO");

            if (payload.get("costo") != null) {
                mp.setCosto(new BigDecimal(payload.get("costo").toString()));
            }
            if (payload.get("cantidadActual") != null) {
                mp.setCantidadActual(parsearIntegerSeguro(payload.get("cantidadActual")));
            }

            Integer idCategoria = extraerIdCategoria(payload);
            materia_prima guardada = materiaPrimaService.guardar(mp, idCategoria);

            return ResponseEntity.status(HttpStatus.CREATED).body(guardada);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @RequestBody Map<String, Object> payload) {
        try {
            materia_prima mp = new materia_prima();
            mp.setNombre((String) payload.get("nombre"));
            mp.setDescripcion((String) payload.get("descripcion"));
            mp.setUnidadMedida((String) payload.get("unidadMedida"));
            
            if (payload.get("estatus") != null) {
                mp.setEstatus(payload.get("estatus").toString());
            }
            if (payload.get("costo") != null) {
                mp.setCosto(new BigDecimal(payload.get("costo").toString()));
            }
            if (payload.get("cantidadActual") != null) {
                mp.setCantidadActual(parsearIntegerSeguro(payload.get("cantidadActual")));
            }

            Integer idCategoria = extraerIdCategoria(payload);
            materia_prima actualizada = materiaPrimaService.actualizar(id, mp, idCategoria);

            return ResponseEntity.ok(actualizada);
        } catch (Exception e) {
            e.printStackTrace(); // Imprime el error exacto en la consola de Spring Boot
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error en solicitud: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private Integer extraerIdCategoria(Map<String, Object> payload) {
        if (payload == null) return null;

        Object idDirecto = payload.get("idCategoria");
        if (idDirecto != null) {
            return parsearIntegerSeguro(idDirecto);
        }

        if (payload.get("categoria") instanceof Map) {
            Map<String, Object> catMap = (Map<String, Object>) payload.get("categoria");
            if (catMap.get("idCategoria") != null) {
                return parsearIntegerSeguro(catMap.get("idCategoria"));
            }
            if (catMap.get("id") != null) {
                return parsearIntegerSeguro(catMap.get("id"));
            }
        }
        return null;
    }

    private Integer parsearIntegerSeguro(Object val) {
        if (val == null) return null;
        try {
            if (val instanceof Number) {
                return ((Number) val).intValue();
            }
            String strVal = val.toString().trim();
            if (strVal.contains(".")) {
                return (int) Double.parseDouble(strVal);
            }
            return Integer.parseInt(strVal);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    @GetMapping("/unidades-medida")
public List<String> obtenerUnidadesMedida() {
    return materiaPrimaService.obtenerUnidadesMedidaUnicas();
}
}