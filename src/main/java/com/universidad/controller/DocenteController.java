package com.universidad.controller;

import com.universidad.dto.DocenteDTO;
import com.universidad.dto.MateriaDTO;
import com.universidad.service.IDocenteService;
import com.universidad.service.IMateriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/docentes")
@RequiredArgsConstructor
public class DocenteController {

    private final IDocenteService docenteService;
    private final IMateriaService materiaService;

    @GetMapping
    public ResponseEntity<List<DocenteDTO>> listar() {
        return ResponseEntity.ok(docenteService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocenteDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(docenteService.obtenerPorId(id));
    }

    @GetMapping("/{id}/materias")
    public ResponseEntity<List<MateriaDTO>> obtenerMateriasPorDocente(@PathVariable Long id) {
        return ResponseEntity.ok(materiaService.obtenerMateriasPorDocente(id));
    }

    @PostMapping
    public ResponseEntity<DocenteDTO> crear(@RequestBody DocenteDTO dto) {
        return ResponseEntity.ok(docenteService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DocenteDTO> actualizar(@PathVariable Long id, @RequestBody DocenteDTO dto) {
        return ResponseEntity.ok(docenteService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        docenteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}