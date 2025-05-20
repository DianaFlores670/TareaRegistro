package com.universidad.service.impl;

import com.universidad.dto.InscripcionDTO;
import com.universidad.model.Estudiante;
import com.universidad.model.Inscripcion;
import com.universidad.model.Materia;
import com.universidad.repository.EstudianteRepository;
import com.universidad.repository.InscripcionRepository;
import com.universidad.repository.MateriaRepository;
import com.universidad.service.InscripcionService;
import com.universidad.validation.InscripcionValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InscripcionServiceImpl implements InscripcionService {

    @Autowired
    private InscripcionValidator inscripcionValidator;

    @Autowired
    private InscripcionRepository inscripcionRepository;

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private MateriaRepository materiaRepository;

    // Cache: lista de todas las inscripciones
    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "inscripciones")
    public List<InscripcionDTO> obtenerTodas() {
        List<Inscripcion> inscripciones = inscripcionRepository.findAll();
        return inscripciones.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Cache: inscripciones por idEstudiante
    @Override
    @Cacheable(value = "inscripcionPorEstudiante", key = "#idEstudiante")
    public InscripcionDTO obtenerInscripcionPorEstudiante(Long idEstudiante) {
        List<Inscripcion> inscripciones = inscripcionRepository.findByEstudianteId(idEstudiante);

        if (inscripciones.isEmpty()) {
            throw new RuntimeException("No se encontraron inscripciones para el estudiante con ID: " + idEstudiante);
        }

        Estudiante estudiante = inscripciones.get(0).getEstudiante();

        InscripcionDTO dto = new InscripcionDTO();
        dto.setIdEstudiante(estudiante.getId());
        dto.setNombreEstudiante(estudiante.getNombre());
        dto.setApellidoEstudiante(estudiante.getApellido());
        dto.setMaterias(
                inscripciones.stream()
                        .map(i -> i.getMateria().getNombreMateria())
                        .collect(Collectors.toList()));

        return dto;
    }

    // Crear inscripcion: actualiza cache de todas inscripciones y de inscripcionPorEstudiante
    @Override
    @Transactional
    @CachePut(value = "inscripcionPorEstudiante", key = "#result.idEstudiante")
    @CacheEvict(value = "inscripciones", allEntries = true)
    public InscripcionDTO crearInscripcion(InscripcionDTO dto) {
        inscripcionValidator.validarCamposNoNulos(dto);
        inscripcionValidator.validarDuplicadoAlCrear(dto);

        Estudiante estudiante = estudianteRepository.findById(dto.getIdEstudiante())
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));
        Materia materia = materiaRepository.findById(dto.getIdMateria())
                .orElseThrow(() -> new RuntimeException("Materia no encontrada"));

        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setEstudiante(estudiante);
        inscripcion.setMateria(materia);

        inscripcion = inscripcionRepository.save(inscripcion);

        return convertirADTO(inscripcion);
    }

    // Actualizar inscripcion: actualiza cache de inscripcionPorEstudiante y limpia cache de todas
    @Override
    @Transactional
    @CachePut(value = "inscripcionPorEstudiante", key = "#result.idEstudiante")
    @CacheEvict(value = "inscripciones", allEntries = true)
    public InscripcionDTO actualizarInscripcion(Long idInscripcion, InscripcionDTO dto) {
        Inscripcion inscripcion = inscripcionRepository.findById(idInscripcion)
                .orElseThrow(() -> new RuntimeException("Inscripción no encontrada"));

        inscripcionValidator.validarCamposNoNulos(dto);
        inscripcionValidator.validarDuplicadoAlActualizar(idInscripcion, dto);

        if (!dto.getIdEstudiante().equals(inscripcion.getEstudiante().getId())) {
            Estudiante estudiante = estudianteRepository.findById(dto.getIdEstudiante())
                    .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));
            inscripcion.setEstudiante(estudiante);
        }

        if (!dto.getIdMateria().equals(inscripcion.getMateria().getId())) {
            Materia materia = materiaRepository.findById(dto.getIdMateria())
                    .orElseThrow(() -> new RuntimeException("Materia no encontrada"));
            inscripcion.setMateria(materia);
        }

        inscripcion = inscripcionRepository.save(inscripcion);
        return convertirADTO(inscripcion);
    }

    // Eliminar inscripcion: limpia cache completa (porque no sabemos qué estudiante afectó)
    @Override
    @Transactional
    @CacheEvict(value = {"inscripciones", "inscripcionPorEstudiante"}, allEntries = true)
    public void eliminarInscripcion(Long idInscripcion) {
        if (!inscripcionRepository.existsById(idInscripcion)) {
            throw new RuntimeException("Inscripción no encontrada");
        }
        inscripcionRepository.deleteById(idInscripcion);
    }

    private InscripcionDTO convertirADTO(Inscripcion inscripcion) {
        InscripcionDTO dto = new InscripcionDTO();
        dto.setId(inscripcion.getId());
        dto.setIdEstudiante(inscripcion.getEstudiante().getId());
        dto.setNombreEstudiante(inscripcion.getEstudiante().getNombre());
        dto.setApellidoEstudiante(inscripcion.getEstudiante().getApellido());
        dto.setIdMateria(inscripcion.getMateria().getId());
        dto.setMaterias(List.of(inscripcion.getMateria().getNombreMateria()));
        return dto;
    }
}
