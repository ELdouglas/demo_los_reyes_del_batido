package com.example.myapp.service;

import com.example.myapp.model.Detalles;
import com.example.myapp.model.DetallesId;
import com.example.myapp.model.Factura;
import com.example.myapp.model.Producto;
import com.example.myapp.model.ProductoMateriaPrima;
import com.example.myapp.model.materia_prima;
import com.example.myapp.repository.FacturaRepository;
import com.example.myapp.repository.MateriaPrimaRepository;
import com.example.myapp.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class FacturaService {

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private MateriaPrimaRepository materiaPrimaRepository;

    @Transactional
    public Factura crearFactura(Map<String, Object> payload) {
        Factura factura = new Factura();
        factura.setFechaEmision(LocalDateTime.now());

        // 1. Lectura segura de encabezado
        Object tasaObj = payload.get("tasaCambio");
        BigDecimal tasa = (tasaObj != null) ? new BigDecimal(tasaObj.toString()) : BigDecimal.ONE;
        factura.setTasaCambio(tasa);

        factura.setMetodoPago(payload.getOrDefault("metodoPago", "Efectivo").toString());
        
        String estatus = payload.getOrDefault("estatus", "COMPLETADA").toString();
        factura.setEstatus(estatus);
        factura.setEstado(payload.getOrDefault("estado", "COMPLETADA").toString());

        if (payload.containsKey("numeroFactura") && payload.get("numeroFactura") != null) {
            factura.setNumeroFactura(payload.get("numeroFactura").toString());
        } else {
            factura.setNumeroFactura(String.valueOf(System.currentTimeMillis()));
        }

        List<Map<String, Object>> detallesJson = (List<Map<String, Object>>) payload.get("detalles");

        // Consolida el consumo total mapeando ID de Materia Prima -> Cantidad Acumulada
        Map<Integer, Double> insumosNecesarios = new HashMap<>();

        if (detallesJson != null) {
            for (Map<String, Object> itemMap : detallesJson) {

                // 2. Extracción de ID del producto
                Integer idProducto = null;
                if (itemMap.containsKey("idProducto") && itemMap.get("idProducto") != null) {
                    idProducto = Integer.parseInt(itemMap.get("idProducto").toString());
                } else if (itemMap.containsKey("idProductos") && itemMap.get("idProductos") != null) {
                    idProducto = Integer.parseInt(itemMap.get("idProductos").toString());
                } else if (itemMap.get("id") instanceof Map) {
                    Map<?, ?> idMap = (Map<?, ?>) itemMap.get("id");
                    if (idMap.containsKey("idProductos") && idMap.get("idProductos") != null) {
                        idProducto = Integer.parseInt(idMap.get("idProductos").toString());
                    } else if (idMap.containsKey("idProducto") && idMap.get("idProducto") != null) {
                        idProducto = Integer.parseInt(idMap.get("idProducto").toString());
                    }
                }

                if (idProducto == null) {
                    throw new IllegalArgumentException("El ítem de la factura no contiene un ID de producto válido");
                }

                // 3. Extracción de cantidad
                Integer cantidad = 1;
                if (itemMap.get("cantidad") != null) {
                    cantidad = Integer.parseInt(itemMap.get("cantidad").toString());
                } else if (itemMap.get("cantidadVendida") != null) {
                    cantidad = Integer.parseInt(itemMap.get("cantidadVendida").toString());
                }

                // 4. Búsqueda de producto
                Optional<Producto> productoOpt = productoRepository.findById(idProducto);
                if (productoOpt.isEmpty()) {
                    throw new IllegalArgumentException("Producto no encontrado con ID: " + idProducto);
                }
                Producto producto = productoOpt.get();

                // 5. Acumulación de insumos requeridos según la receta
                if ("COMPLETADA".equalsIgnoreCase(estatus)) {
                    if (producto.getRecetas() != null) {
                        for (ProductoMateriaPrima ingrediente : producto.getRecetas()) {
                            materia_prima mp = ingrediente.getMateriaPrima();
                            
                            if (mp != null && mp.getIdMateriaPrima() != null) {
                                double requeridoPorUnidad = 0.0;
                                if (ingrediente.getCantidadRequerida() != null) {
                                    requeridoPorUnidad = ingrediente.getCantidadRequerida().doubleValue();
                                }
                                                                                
                                double totalIngrediente = requeridoPorUnidad * cantidad;
                                Integer idMp = mp.getIdMateriaPrima();

                                insumosNecesarios.put(idMp, insumosNecesarios.getOrDefault(idMp, 0.0) + totalIngrediente);
                            }
                        }
                    }
                }

                // Vínculo del detalle
                Detalles detalle = new Detalles();
                detalle.setProducto(producto);
                detalle.setCantidadVendida(cantidad);

                if (detalle.getId() == null) {
                    detalle.setId(new DetallesId());
                }
                detalle.getId().setIdProductos(idProducto);

                factura.agregarDetalle(detalle);
            }
        }

        // 6. Validación de estatus (ACTIVO), inventario suficiente y descuento de Stock
        if ("COMPLETADA".equalsIgnoreCase(estatus) && !insumosNecesarios.isEmpty()) {
            List<String> erroresInactivos = new ArrayList<>();
            List<String> erroresStock = new ArrayList<>();
            Map<materia_prima, Double> materiasAActualizar = new HashMap<>();

            // Verificar estatus activo y disponibilidad de stock
            for (Map.Entry<Integer, Double> entry : insumosNecesarios.entrySet()) {
                Integer idMp = entry.getKey();
                double requerido = entry.getValue();

                materia_prima mp = materiaPrimaRepository.findById(idMp)
                        .orElseThrow(() -> new IllegalArgumentException("Materia prima no encontrada con ID: " + idMp));

                // A. Validar si la materia prima está inactiva
                String estatusMp = mp.getEstatus(); 
                if (estatusMp != null && "INACTIVO".equalsIgnoreCase(estatusMp.trim())) {
                    erroresInactivos.add(mp.getNombre());
                    continue; // Se omite la revisión de stock para este insumo ya inactivo
                }

                // B. Validar si hay stock suficiente
                double disponible = mp.getCantidad_actual() != null ? mp.getCantidad_actual() : 0;

                if (disponible < requerido) {
                    double faltante = requerido - disponible;
                    erroresStock.add(mp.getNombre() + " (Faltan " + String.format("%.2f", faltante) + " " + mp.getUnidadMedida() + ")");
                } else {
                    materiasAActualizar.put(mp, requerido);
                }
            }

            // Si hay materias primas inactivas, se cancela la transacción lanzando la excepción
            if (!erroresInactivos.isEmpty()) {
                throw new IllegalArgumentException("Materia prima inactiva: " + String.join(", ", erroresInactivos));
            }

            // Si faltan insumos por stock insuficiente, se cancela la transacción
            if (!erroresStock.isEmpty()) {
                throw new IllegalArgumentException("Falta materia prima: " + String.join(", ", erroresStock));
            }

            // Aplicar descuento en la base de datos únicamente si todo está ACTIVO y SUFICIENTE
            for (Map.Entry<materia_prima, Double> entry : materiasAActualizar.entrySet()) {
                materia_prima mp = entry.getKey();
                double requerido = entry.getValue();
                
                int stockActual = mp.getCantidad_actual() != null ? mp.getCantidad_actual() : 0;
                int nuevoStock = (int) Math.round(stockActual - requerido);
                
                mp.setCantidadActual(nuevoStock);
                materiaPrimaRepository.save(mp);
            }
        }

        return facturaRepository.save(factura);
    }

    public List<Factura> obtenerTodas() {
        return facturaRepository.findAll();
    }
}