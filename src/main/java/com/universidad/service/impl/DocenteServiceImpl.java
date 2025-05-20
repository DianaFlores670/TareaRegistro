package com.universidad.service.impl;

import com.universidad.dto.DocenteDTO;
import com.universidad.model.Docente;
import com.universidad.model.Materia;
import com.universidad.repository.DocenteRepository;
import com.universidad.service.IDocenteService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocenteServiceImpl implements IDocenteService {

    private final DocenteRepository docenteRepository;

    private DocenteDTO mapToDTO(Docente docente) {
        return DocenteDTO.builder()
                .id(docente.getId())
                .nombre(docente.getNombre())
                .apellido(docente.getApellido())
                .email(docente.getEmail())
                .fechaNacimiento(docente.getFechaNacimiento())
                .nroEmpleado(docente.getNroEmpleado())
                .departamento(docente.getDepartamento())
                .materias(
                    docente.getMaterias() == null
                        ? List.of()
                        : docente.getMaterias().stream()
                            .map(Materia::getId)
                            .collect(Collectors.toList())
                )
                .build();
    }

    private Docente mapToEntity(DocenteDTO dto) {
        Docente docente = Docente.builder()
                .id(dto.getId())
                .nombre(dto.getNombre())
                .apellido(dto.getApellido())
                .email(dto.getEmail())
                .fechaNacimiento(dto.getFechaNacimiento())
                .nroEmpleado(dto.getNroEmpleado())
                .departamento(dto.getDepartamento())
                .build();

        docente.setMaterias(new ArrayList<>());
        return docente;
    }

    @Override
    @Cacheable(value = "docentes")
    public List<DocenteDTO> listarTodos() {
        return docenteRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "docente", key = "#id")
    public DocenteDTO obtenerPorId(Long id) {
        Docente docente = docenteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Docente no encontrado con ID: " + id));
        return mapToDTO(docente);
    }

    @Override
    @CachePut(value = "docente", key = "#result.id")
    @CacheEvict(value = "docentes", allEntries = true)
    public DocenteDTO crear(DocenteDTO dto) {
        Docente docente = mapToEntity(dto);
        return mapToDTO(docenteRepository.save(docente));
    }

    @Override
    @CachePut(value = "docente", key = "#id")
    @CacheEvict(value = "docentes", allEntries = true)
    public DocenteDTO actualizar(Long id, DocenteDTO dto) {
        Docente existente = docenteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Docente no encontrado con ID: " + id));

        existente.setNombre(dto.getNombre());
        existente.setApellido(dto.getApellido());
        existente.setEmail(dto.getEmail());
        existente.setNroEmpleado(dto.getNroEmpleado());
        existente.setDepartamento(dto.getDepartamento());

        return mapToDTO(docenteRepository.save(existente));
    }

    @Override
    @CacheEvict(value = {"docente", "docentes"}, allEntries = true)
    public void eliminar(Long id) {
        if (!docenteRepository.existsById(id)) {
            throw new EntityNotFoundException("Docente no encontrado con ID: " + id);
        }
        docenteRepository.deleteById(id);
    }

    @Override
    @Cacheable(value = "materiasPorDocente", key = "#docenteId")
    public List<Long> obtenerMateriasPorDocente(Long docenteId) {
        Docente docente = docenteRepository.findById(docenteId)
                .orElseThrow(() -> new EntityNotFoundException("Docente no encontrado con ID: " + docenteId));
        List<Materia> materias = docente.getMaterias();
        if (materias == null) return List.of();

        return materias.stream()
                .map(Materia::getId)
                .collect(Collectors.toList());
    }
}
