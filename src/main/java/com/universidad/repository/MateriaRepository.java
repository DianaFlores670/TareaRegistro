package com.universidad.repository;

import com.universidad.model.Materia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Lock;

import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

@Repository
public interface MateriaRepository extends JpaRepository<Materia, Long> {

    // Buscar por código único (por ejemplo, código interno de la materia)
    Materia findByCodigoUnico(String codigoUnico);

    // Búsqueda con bloqueo pesimista para operaciones críticas
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Materia> findById(Long id);

    // Verificar si ya existe una materia con ese nombre (para validaciones)
    boolean existsByNombreMateria(String nombreMateria);

    // Verificar si ya existe una materia con ese código único (para validaciones)
    boolean existsByCodigoUnico(String codigoUnico);

    // Buscar todas las materias asignadas a un docente por su ID
    List<Materia> findByDocenteId(Long docenteId);
}
