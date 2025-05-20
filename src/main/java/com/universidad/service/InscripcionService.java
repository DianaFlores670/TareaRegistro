package com.universidad.service;

import com.universidad.dto.InscripcionDTO;
import java.util.List;

public interface InscripcionService {
    InscripcionDTO crearInscripcion(InscripcionDTO dto);

    InscripcionDTO obtenerInscripcionPorEstudiante(Long idEstudiante);

    InscripcionDTO actualizarInscripcion(Long idInscripcion, InscripcionDTO dto);

    void eliminarInscripcion(Long idInscripcion);

    List<InscripcionDTO> obtenerTodas(); // opcional, para listar todas
}
