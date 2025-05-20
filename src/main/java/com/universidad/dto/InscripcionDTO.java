package com.universidad.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class InscripcionDTO implements Serializable {

    // ID de la inscripción
    private Long id;

    // Para crear/actualizar inscripción
    private Long idEstudiante;
    private Long idMateria;

    // Para respuesta GET
    private String nombreEstudiante;
    private String apellidoEstudiante;
    private List<String> materias;
}
