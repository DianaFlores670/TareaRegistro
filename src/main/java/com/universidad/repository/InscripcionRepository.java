package com.universidad.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.universidad.model.Inscripcion;

public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {
    // Busca todas las inscripciones de un estudiante dado su ID
    List<Inscripcion> findByEstudianteId(Long idEstudiante);

    boolean existsByEstudianteIdAndMateriaId(Long estudianteId, Long materiaId);

    boolean existsByEstudianteIdAndMateriaIdAndIdNot(Long estudianteId, Long materiaId, Long idInscripcion);

    // Agrega este método para buscar inscripción por estudiante y materia
    Optional<Inscripcion> findByEstudianteIdAndMateriaId(Long estudianteId, Long materiaId);

}
