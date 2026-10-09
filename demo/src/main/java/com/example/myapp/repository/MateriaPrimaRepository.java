package com.example.myapp.repository;

import com.example.myapp.model.materia_prima;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MateriaPrimaRepository extends JpaRepository<materia_prima, Integer> {

    @Query("SELECT DISTINCT m.unidadMedida FROM materia_prima m WHERE m.unidadMedida IS NOT NULL AND m.unidadMedida <> ''")
    List<String> findDistinctUnidadesMedida();
}