package com.universidad.service;

import com.universidad.dto.DocenteDTO;
import java.util.List;

public interface IDocenteService {
    List<DocenteDTO> listarTodos();
    DocenteDTO obtenerPorId(Long id);
    DocenteDTO crear(DocenteDTO docenteDTO);
    DocenteDTO actualizar(Long id, DocenteDTO docenteDTO);
    void eliminar(Long id);

    /** 
     * Obtiene las materias que imparte un docente 
     */
    List<Long> obtenerMateriasPorDocente(Long docenteId);
}