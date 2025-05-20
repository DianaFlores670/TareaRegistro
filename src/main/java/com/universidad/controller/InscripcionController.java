package com.universidad.controller;

import com.universidad.dto.InscripcionDTO;
import com.universidad.service.InscripcionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inscripciones")
public class InscripcionController {

    @Autowired
    private InscripcionService inscripcionService;

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody InscripcionDTO dto) {
        try {
            InscripcionDTO creado = inscripcionService.crearInscripcion(dto);
            return ResponseEntity.ok(creado);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", ex.getMessage()));
        }
    }

    @GetMapping("/estudiante/{id}")
    public ResponseEntity<?> obtenerPorEstudiante(@PathVariable Long id) {
        try {
            InscripcionDTO dto = inscripcionService.obtenerInscripcionPorEstudiante(id);
            return ResponseEntity.ok(dto);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", ex.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody InscripcionDTO dto) {
        try {
            InscripcionDTO actualizado = inscripcionService.actualizarInscripcion(id, dto);
            return ResponseEntity.ok(actualizado);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", ex.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            inscripcionService.eliminarInscripcion(id);
            return ResponseEntity.noContent().build();
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", ex.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<InscripcionDTO>> listarTodas() {
        return ResponseEntity.ok(inscripcionService.obtenerTodas());
    }
}
