package com.universidad.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocenteDTO implements Serializable {
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String nroEmpleado;
    private String departamento;

    /** 
     * Fecha de nacimiento del docente 
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fechaNacimiento;

    /** 
     * Lista de IDs de materias que el docente imparte 
     */
    private List<Long> materias;

    
}