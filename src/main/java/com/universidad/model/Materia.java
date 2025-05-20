package com.universidad.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "materia")
public class Materia implements Serializable {

    private static final long serialVersionUID = 1L;

    public Materia(Long id, String nombreMateria, String codigoUnico) {
        this.id = id;
        this.nombreMateria = nombreMateria;
        this.codigoUnico = codigoUnico;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_materia")
    private Long id;

    @Column(name = "nombre_materia", nullable = false, length = 100)
    private String nombreMateria;

    @Column(name = "codigo_unico", nullable = false, unique = true)
    private String codigoUnico;

    @Column(name = "creditos", nullable = false)
    private Integer creditos;

    @Version
    private Long version;

    // Relación con Docente
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "docente_id", nullable = false)
    private Docente docente;

    // Materias que esta materia necesita como prerequisitos
    @ManyToMany
    @JoinTable(
        name = "materia_prerequisito",
        joinColumns = @JoinColumn(name = "id_materia"),
        inverseJoinColumns = @JoinColumn(name = "id_prerequisito")
    )
    private List<Materia> prerequisitos;

    // Materias que tienen esta como prerequisito
    @ManyToMany(mappedBy = "prerequisitos")
    private List<Materia> esPrerequisitoDe;

    /**
     * Verifica si agregar una materia como prerequisito crearía un ciclo.
     *
     * @param nuevaId La ID de la materia candidata a ser agregada como prerequisito.
     * @return true si forma un ciclo, false si no.
     */
    public boolean formariaCirculo(Long nuevaId) {
        return contieneEnRecursivo(this, nuevaId, new HashSet<>());
    }

    /**
     * Busca recursivamente si la materia candidata ya depende indirectamente de esta.
     *
     * @param actual La materia actual en la exploración.
     * @param objetivoId La ID de la materia original.
     * @param visitados IDs ya visitados para evitar ciclos infinitos.
     * @return true si encuentra un ciclo, false si no.
     */
    private boolean contieneEnRecursivo(Materia actual, Long objetivoId, Set<Long> visitados) {
        if (actual == null || actual.getId() == null) return false;
        if (!visitados.add(actual.getId())) return false;

        if (actual.getPrerequisitos() != null) {
            for (Materia prereq : actual.getPrerequisitos()) {
                if (prereq != null && prereq.getId() != null) {
                    if (prereq.getId().equals(objetivoId)) {
                        return true;
                    }
                    if (contieneEnRecursivo(prereq, objetivoId, visitados)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
