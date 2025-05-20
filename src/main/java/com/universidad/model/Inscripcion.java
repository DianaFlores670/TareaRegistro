package com.universidad.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "inscripcion") // Nombre real de la tabla
public class Inscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID autogenerado
    private Long id;

    // Relación con Estudiante
    @ManyToOne
    @JoinColumn(name = "id_estudiante", nullable = false) // FK en la tabla inscripcion
    private Estudiante estudiante;

    // Relación con Materia
    @ManyToOne
    @JoinColumn(name = "id_materia", nullable = false) // FK en la tabla inscripcion
    private Materia materia;
}
