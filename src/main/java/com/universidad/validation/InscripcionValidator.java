package com.universidad.validation;

import com.universidad.dto.InscripcionDTO;
import com.universidad.repository.InscripcionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InscripcionValidator {

    @Autowired
    private InscripcionRepository inscripcionRepository;

    public void validarCamposNoNulos(InscripcionDTO dto) {
        if (dto.getIdEstudiante() == null) {
            throw new IllegalArgumentException("El idEstudiante es obligatorio");
        }
        if (dto.getIdMateria() == null) {
            throw new IllegalArgumentException("El idMateria es obligatorio");
        }
    }

    public void validarDuplicadoAlCrear(InscripcionDTO dto) {
        boolean existe = inscripcionRepository.existsByEstudianteIdAndMateriaId(dto.getIdEstudiante(),
                dto.getIdMateria());
        if (existe) {
            throw new IllegalArgumentException("El estudiante ya está inscrito en esta materia");
        }
    }

    public void validarDuplicadoAlActualizar(Long idInscripcion, InscripcionDTO dto) {
        boolean existe = inscripcionRepository.existsByEstudianteIdAndMateriaIdAndIdNot(dto.getIdEstudiante(),
                dto.getIdMateria(), idInscripcion);
        if (existe) {
            throw new IllegalArgumentException("Ya existe otra inscripción con este estudiante y materia");
        }
    }
}
