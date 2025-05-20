package com.universidad.service.impl;

import com.universidad.model.Materia;
import com.universidad.model.Docente;
import com.universidad.repository.MateriaRepository;
import com.universidad.repository.DocenteRepository;
import com.universidad.service.IMateriaService;
import com.universidad.validation.MateriaValidator;

import jakarta.persistence.EntityNotFoundException;

import com.universidad.dto.MateriaDTO;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MateriaServiceImpl implements IMateriaService {

    private final MateriaRepository materiaRepository;
    private final DocenteRepository docenteRepository;
    private final MateriaValidator materiaValidator;

    private MateriaDTO mapToDTO(Materia materia) {
        return MateriaDTO.builder()
                .id(materia.getId())
                .nombreMateria(materia.getNombreMateria())
                .codigoUnico(materia.getCodigoUnico())
                .creditos(materia.getCreditos())
                .idDocente(materia.getDocente() != null ? materia.getDocente().getId() : null)
                .prerequisitos(
                        materia.getPrerequisitos() == null
                                ? List.of()
                                : materia.getPrerequisitos().stream()
                                        .map(Materia::getId)
                                        .collect(Collectors.toList()))
                .esPrerequisitoDe(
                        materia.getEsPrerequisitoDe() == null
                                ? List.of()
                                : materia.getEsPrerequisitoDe().stream()
                                        .map(Materia::getId)
                                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    @Transactional
    @Cacheable(value = "materias")
    public List<MateriaDTO> obtenerTodasLasMaterias() {
        return materiaRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    @Cacheable(value = "materia", key = "#id")
    public MateriaDTO obtenerMateriaPorId(Long id) {
        return materiaRepository.findById(id).map(this::mapToDTO).orElse(null);
    }

    @Override
    @Cacheable(value = "materiaPorCodigoUnico", key = "#codigoUnico")
    public MateriaDTO obtenerMateriaPorCodigoUnico(String codigoUnico) {
        Materia materia = materiaRepository.findByCodigoUnico(codigoUnico);
        if (materia == null) {
            throw new EntityNotFoundException("Materia no encontrada con código único: " + codigoUnico);
        }
        return mapToDTO(materia);
    }

    @Override
    @Cacheable(value = "materiasPorDocente", key = "#docenteId")
    public List<MateriaDTO> obtenerMateriasPorDocente(Long docenteId) {
        return materiaRepository.findByDocenteId(docenteId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @CachePut(value = "materia", key = "#result.id")
    @CacheEvict(value = { "materias", "materiasPorDocente", "materiaPorCodigoUnico" }, allEntries = true)
    public MateriaDTO crearMateria(MateriaDTO materiaDTO) {
        materiaValidator.validarDatosMateria(materiaDTO);

        Docente docente = docenteRepository.findById(materiaDTO.getIdDocente())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Docente no encontrado con ID: " + materiaDTO.getIdDocente()));

        Materia materia = new Materia();
        materia.setNombreMateria(materiaDTO.getNombreMateria());
        materia.setCodigoUnico(materiaDTO.getCodigoUnico());
        materia.setCreditos(materiaDTO.getCreditos());
        materia.setDocente(docente);

        return mapToDTO(materiaRepository.save(materia));
    }

    @Override
    @Transactional
    @CachePut(value = "materia", key = "#id")
    @CacheEvict(value = { "materias", "materiasPorDocente", "materiaPorCodigoUnico" }, allEntries = true)
    public MateriaDTO actualizarMateria(Long id, MateriaDTO materiaDTO) {
        materiaValidator.validarDatosMateria(materiaDTO);

        Materia materia = materiaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Materia no encontrada con ID: " + id));

        Docente docente = docenteRepository.findById(materiaDTO.getIdDocente())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Docente no encontrado con ID: " + materiaDTO.getIdDocente()));

        materia.setNombreMateria(materiaDTO.getNombreMateria());
        materia.setCodigoUnico(materiaDTO.getCodigoUnico());
        materia.setCreditos(materiaDTO.getCreditos());
        materia.setDocente(docente);

        if (materiaDTO.getPrerequisitos() != null) {
            List<Materia> prerequisitos = materiaDTO.getPrerequisitos().stream()
                    .map(prereqId -> materiaRepository.findById(prereqId)
                            .orElseThrow(() -> new EntityNotFoundException(
                                    "Prerequisito no encontrado con ID: " + prereqId)))
                    .collect(Collectors.toList());
            materia.setPrerequisitos(prerequisitos);
        } else {
            materia.setPrerequisitos(List.of());
        }

        if (materiaDTO.getEsPrerequisitoDe() != null) {
            List<Materia> esPrerequisitoDe = materiaDTO.getEsPrerequisitoDe().stream()
                    .map(idEsPre -> materiaRepository.findById(idEsPre)
                            .orElseThrow(() -> new EntityNotFoundException(
                                    "Materia no encontrada en esPrerequisitoDe con ID: " + idEsPre)))
                    .collect(Collectors.toList());
            materia.setEsPrerequisitoDe(esPrerequisitoDe);
        } else {
            materia.setEsPrerequisitoDe(List.of());
        }

        return mapToDTO(materiaRepository.save(materia));
    }

    @Override
    @CacheEvict(value = { "materia", "materias", "materiasPorDocente", "materiaPorCodigoUnico" }, allEntries = true)
    public void eliminarMateria(Long id) {
        materiaRepository.deleteById(id);
    }
}
