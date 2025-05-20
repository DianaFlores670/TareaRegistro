package com.universidad.validation;

import com.universidad.dto.MateriaDTO;
import com.universidad.repository.DocenteRepository;
import com.universidad.repository.MateriaRepository;
import com.universidad.model.Materia;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class MateriaValidator {

    private final DocenteRepository docenteRepository;
    private final MateriaRepository materiaRepository;

    public MateriaValidator(DocenteRepository docenteRepository, MateriaRepository materiaRepository) {
        this.docenteRepository = docenteRepository;
        this.materiaRepository = materiaRepository;
    }

    public void validarDatosMateria(MateriaDTO materiaDTO) {
        if (materiaDTO.getNombreMateria() == null || materiaDTO.getNombreMateria().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la materia es obligatorio.");
        }

        if (materiaDTO.getCodigoUnico() == null || materiaDTO.getCodigoUnico().trim().isEmpty()) {
            throw new IllegalArgumentException("El código único es obligatorio.");
        }

        // Evitar error en actualización por código ya existente en otra materia
        Materia existente = materiaRepository.findByCodigoUnico(materiaDTO.getCodigoUnico());
        if (existente != null) {
            if (materiaDTO.getId() == null || !existente.getId().equals(materiaDTO.getId())) {
                throw new IllegalArgumentException("Ya existe una materia con el código único: " + materiaDTO.getCodigoUnico());
            }
        }

        if (materiaDTO.getIdDocente() == null) {
            throw new IllegalArgumentException("El ID del docente es obligatorio.");
        }

        if (!docenteRepository.existsById(materiaDTO.getIdDocente())) {
            throw new EntityNotFoundException("Docente no encontrado con ID: " + materiaDTO.getIdDocente());
        }
    }
}
