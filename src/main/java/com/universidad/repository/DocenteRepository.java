package com.universidad.repository;

import com.universidad.model.Docente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DocenteRepository extends JpaRepository<Docente, Long> {
    boolean existsByEmail(String email);
    boolean existsByNroEmpleado(String nroEmpleado);

    /** 
     * Obtiene un docente con sus materias 
     */
    @Query("SELECT d FROM Docente d LEFT JOIN FETCH d.materias WHERE d.id = :id")
    Docente findDocenteWithMaterias(@Param("id") Long id);

    
}