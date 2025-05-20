package com.universidad.controller;

import com.universidad.service.IMateriaService;
import com.universidad.dto.MateriaDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.RequiredArgsConstructor;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/materias")
@RequiredArgsConstructor
public class MateriaController {

    private final IMateriaService materiaService;
    private static final Logger logger = LoggerFactory.getLogger(MateriaController.class);

    @GetMapping
    public ResponseEntity<List<MateriaDTO>> obtenerTodasLasMaterias() {
        long inicio = System.currentTimeMillis();
        logger.info("[MATERIA] Inicio obtenerTodasLasMaterias: {}", inicio);
        List<MateriaDTO> result = materiaService.obtenerTodasLasMaterias();
        long fin = System.currentTimeMillis();
        logger.info("[MATERIA] Fin obtenerTodasLasMaterias: {} (Duración: {} ms)", fin, (fin-inicio));
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MateriaDTO> obtenerMateriaPorId(@PathVariable Long id) {
        long inicio = System.currentTimeMillis();
        logger.info("[MATERIA] Inicio obtenerMateriaPorId: {}", inicio);
        MateriaDTO materia = materiaService.obtenerMateriaPorId(id);
        long fin = System.currentTimeMillis();
        logger.info("[MATERIA] Fin obtenerMateriaPorId: {} (Duración: {} ms)", fin, (fin-inicio));

        return ResponseEntity.ok(materia);
    }

    @PostMapping
    public ResponseEntity<MateriaDTO> crearMateria(@RequestBody @Valid MateriaDTO materia) {
        logger.info("[MATERIA] Crear materia: {}", materia);
        MateriaDTO nueva = materiaService.crearMateria(materia);
        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MateriaDTO> actualizarMateria(@PathVariable Long id, @RequestBody @Valid MateriaDTO materia) {
        logger.info("[MATERIA] Actualizar materia ID {}: {}", id, materia);
        MateriaDTO actualizadaDTO = materiaService.actualizarMateria(id, materia);
        return ResponseEntity.ok(actualizadaDTO);
}

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMateria(@PathVariable Long id) {
        logger.info("[MATERIA] Eliminar materia ID {}", id);
        materiaService.eliminarMateria(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/docente/{docenteId}")
    public ResponseEntity<List<MateriaDTO>> obtenerMateriasPorDocente(@PathVariable Long docenteId) {
        logger.info("[MATERIA] Obtener materias por docente ID {}", docenteId);
        return ResponseEntity.ok(materiaService.obtenerMateriasPorDocente(docenteId));
    }
}
