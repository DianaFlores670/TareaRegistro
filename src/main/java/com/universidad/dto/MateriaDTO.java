package com.universidad.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MateriaDTO implements Serializable {

    private Long id;

    @NotBlank(message = "El nombre de la materia es obligatorio")
    private String nombreMateria;

    @NotBlank(message = "El código único de la materia es obligatorio")
    private String codigoUnico;

    private Integer creditos; // Ya no es obligatorio

    @NotNull(message = "El ID del docente es obligatorio")
    private Long idDocente;

    private List<Long> prerequisitos;       // No validados
    private List<Long> esPrerequisitoDe;    // No validados
}
