package com.example.myapp.service;

import com.example.myapp.model.Categoria;
import com.example.myapp.model.materia_prima;
import com.example.myapp.repository.CategoriaRepository;
import com.example.myapp.repository.MateriaPrimaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MateriaPrimaService {

    @Autowired
    private MateriaPrimaRepository materiaPrimaRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<materia_prima> listarTodas() {
        return materiaPrimaRepository.findAll();
    }

    public Optional<materia_prima> obtenerPorId(Integer id) {
        return materiaPrimaRepository.findById(id);
    }

    public materia_prima guardar(materia_prima mp, Integer idCategoria) {
        if (idCategoria != null) {
            Categoria cat = categoriaRepository.findById(idCategoria)
                    .orElseThrow(() -> new RuntimeException("La categoría especificada no existe: " + idCategoria));
            mp.setCategoria(cat);
        }
        return materiaPrimaRepository.save(mp);
    }

    public materia_prima actualizar(Integer id, materia_prima datosNuevos, Integer idCategoria) {
        materia_prima mpExistente = materiaPrimaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Materia prima no encontrada con ID: " + id));

        mpExistente.setNombre(datosNuevos.getNombre());
        mpExistente.setDescripcion(datosNuevos.getDescripcion());
        mpExistente.setCosto(datosNuevos.getCosto());
        mpExistente.setCantidadActual(datosNuevos.getCantidadActual());
        mpExistente.setUnidadMedida(datosNuevos.getUnidadMedida());

        if (datosNuevos.getEstatus() != null) {
            mpExistente.setEstatus(datosNuevos.getEstatus());
        }

        if (idCategoria != null) {
            Categoria cat = categoriaRepository.findById(idCategoria)
                    .orElseThrow(() -> new RuntimeException("La categoría especificada no existe: " + idCategoria));
            mpExistente.setCategoria(cat);
        }

        return materiaPrimaRepository.save(mpExistente);
    }
    
    public List<String> obtenerUnidadesMedidaUnicas() {
    return materiaPrimaRepository.findDistinctUnidadesMedida();
}
}