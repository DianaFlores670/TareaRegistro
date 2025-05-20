package com.universidad.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "docente")
public class Docente extends Persona {

    @Column(name = "nro_empleado", nullable = false, unique = true)
    private String nroEmpleado;

    @Column(name = "departamento", nullable = false)
    private String departamento;

    @OneToMany(mappedBy = "docente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EvaluacionDocente> evaluaciones = new ArrayList<>();

    @OneToMany(mappedBy = "docente", cascade = CascadeType.ALL, orphanRemoval = true)
private List<Materia> materias = new ArrayList<>();

}