package com.universidad.service.impl;

import com.universidad.dto.EstudianteDTO;
import com.universidad.model.Estudiante;
import com.universidad.model.Materia;
import com.universidad.repository.EstudianteRepository;
import com.universidad.service.IEstudianteService;
import com.universidad.validation.EstudianteValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EstudianteServiceImpl implements IEstudianteService {

    private final EstudianteRepository estudianteRepository;
    private final EstudianteValidator estudianteValidator;

    @Autowired
    public EstudianteServiceImpl(EstudianteRepository estudianteRepository, EstudianteValidator estudianteValidator) {
        this.estudianteRepository = estudianteRepository;
        this.estudianteValidator = estudianteValidator;
    }

    @Override
    @Cacheable(value = "estudiantes")
    public List<EstudianteDTO> obtenerTodosLosEstudiantes() {
        return estudianteRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "estudiante", key = "#numeroInscripcion")
    public EstudianteDTO obtenerEstudiantePorNumeroInscripcion(String numeroInscripcion) {
        Estudiante estudiante = estudianteRepository.findByNumeroInscripcion(numeroInscripcion);
        if (estudiante == null) {
            throw new RuntimeException("Estudiante no encontrado con número de inscripción: " + numeroInscripcion);
        }
        return convertToDTO(estudiante);
    }

    @Override
    @Cacheable(value = "estudiantesActivos")
    public List<EstudianteDTO> obtenerEstudianteActivo() {
        return estudianteRepository.findAll().stream()
                .filter(est -> "activo".equalsIgnoreCase(est.getEstado()))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "materiasEstudiante", key = "#estudianteId")
    public List<Materia> obtenerMateriasDeEstudiante(Long estudianteId) {
        Estudiante estudiante = estudianteRepository.findById(estudianteId)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado con ID: " + estudianteId));
        return estudiante.getMaterias();
    }

    @Override
    @CachePut(value = "estudiante", key = "#result.numeroInscripcion")
    @CacheEvict(value = {"estudiantes", "estudiantesActivos"}, allEntries = true)
    public EstudianteDTO crearEstudiante(EstudianteDTO estudianteDTO) {
        estudianteValidator.validacionCompletaEstudiante(estudianteDTO);

        Estudiante estudiante = convertToEntity(estudianteDTO);
        Estudiante estudianteGuardado = estudianteRepository.save(estudiante);
        return convertToDTO(estudianteGuardado);
    }

    @Override
    @CachePut(value = "estudiante", key = "#estudianteDTO.numeroInscripcion")
    @CacheEvict(value = {"estudiantes", "estudiantesActivos"}, allEntries = true)
    public EstudianteDTO actualizarEstudiante(Long id, EstudianteDTO estudianteDTO) {
        Estudiante existente = estudianteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado con ID: " + id));

        existente.setNombre(estudianteDTO.getNombre());
        existente.setApellido(estudianteDTO.getApellido());
        existente.setEmail(estudianteDTO.getEmail());
        existente.setFechaNacimiento(estudianteDTO.getFechaNacimiento());
        existente.setNumeroInscripcion(estudianteDTO.getNumeroInscripcion());
        existente.setUsuarioModificacion("admin"); // Idealmente obtener usuario desde contexto de seguridad
        existente.setFechaModificacion(LocalDate.now());

        Estudiante actualizado = estudianteRepository.save(existente);
        return convertToDTO(actualizado);
    }

    @Override
    @CacheEvict(value = {"estudiante", "estudiantes", "estudiantesActivos"}, allEntries = true)
    public EstudianteDTO eliminarEstudiante(Long id, EstudianteDTO estudianteDTO) {
        Estudiante existente = estudianteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado con ID: " + id));

        existente.setEstado("inactivo");
        existente.setUsuarioBaja("admin"); // Igual aquí, ideal obtener usuario actual
        existente.setFechaBaja(LocalDate.now());
        existente.setMotivoBaja(estudianteDTO.getMotivoBaja());

        Estudiante inactivo = estudianteRepository.save(existente);
        return convertToDTO(inactivo);
    }

    @Transactional
    public Estudiante obtenerEstudianteConBloqueo(Long id) {
        Estudiante est = estudianteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado con ID: " + id));
        try {
            Thread.sleep(15000); // Simulación de tiempo prolongado para bloqueo
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return est;
    }

    // Helpers para conversión entre entidad y DTO
    private EstudianteDTO convertToDTO(Estudiante estudiante) {
        return EstudianteDTO.builder()
                .id(estudiante.getId())
                .nombre(estudiante.getNombre())
                .apellido(estudiante.getApellido())
                .email(estudiante.getEmail())
                .fechaNacimiento(estudiante.getFechaNacimiento())
                .numeroInscripcion(estudiante.getNumeroInscripcion())
                .estado(estudiante.getEstado())
                .usuarioAlta(estudiante.getUsuarioAlta())
                .fechaAlta(estudiante.getFechaAlta())
                .usuarioModificacion(estudiante.getUsuarioModificacion())
                .usuarioBaja(estudiante.getUsuarioBaja())
                .fechaBaja(estudiante.getFechaBaja())
                .motivoBaja(estudiante.getMotivoBaja())
                .build();
    }

    private Estudiante convertToEntity(EstudianteDTO dto) {
        return Estudiante.builder()
                .id(dto.getId())
                .nombre(dto.getNombre())
                .apellido(dto.getApellido())
                .email(dto.getEmail())
                .fechaNacimiento(dto.getFechaNacimiento())
                .numeroInscripcion(dto.getNumeroInscripcion())
                .usuarioAlta(dto.getUsuarioAlta())
                .fechaAlta(dto.getFechaAlta())
                .usuarioModificacion(dto.getUsuarioModificacion())
                .fechaModificacion(dto.getFechaModificacion())
                .estado(dto.getEstado())
                .usuarioBaja(dto.getUsuarioBaja())
                .fechaBaja(dto.getFechaBaja())
                .motivoBaja(dto.getMotivoBaja())
                .build();
    }
}
