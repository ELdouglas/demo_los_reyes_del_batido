package com.example.myapp.controller;

import com.example.myapp.model.Factura;
import com.example.myapp.service.FacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/facturas")
@CrossOrigin(origins = "*")
public class FacturaController {

    @Autowired
    private FacturaService facturaService;

    @GetMapping
    public ResponseEntity<List<Factura>> listar() {
        return ResponseEntity.ok(facturaService.obtenerTodas());
    }

    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody Map<String, Object> payload) {
        try {
            Factura registrada = facturaService.crearFactura(payload);
            return ResponseEntity.status(HttpStatus.CREATED).body(registrada);
            
        } catch (IllegalArgumentException e) {
            // Captura los errores de stock insuficiente o IDs no válidos
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
                    
        } catch (Exception e) {
            // Captura cualquier otro error no contemplado
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno en el servidor: " + e.getMessage());
        }
    }
}