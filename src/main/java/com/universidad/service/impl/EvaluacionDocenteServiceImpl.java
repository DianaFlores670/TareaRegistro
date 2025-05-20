package com.universidad.service.impl;

import com.universidad.model.EvaluacionDocente;
import com.universidad.model.Docente;
import com.universidad.repository.EvaluacionDocenteRepository;
import com.universidad.repository.DocenteRepository;
import com.universidad.service.IEvaluacionDocenteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class EvaluacionDocenteServiceImpl implements IEvaluacionDocenteService {

    private final EvaluacionDocenteRepository evaluacionDocenteRepository;
    private final DocenteRepository docenteRepository;

    @Autowired
    public EvaluacionDocenteServiceImpl(EvaluacionDocenteRepository evaluacionDocenteRepository, 
                                        DocenteRepository docenteRepository) {
        this.evaluacionDocenteRepository = evaluacionDocenteRepository;
        this.docenteRepository = docenteRepository;
    }

    @Override
    @CachePut(value = "evaluacionDocente", key = "#result.id")
    @CacheEvict(value = "evaluacionesPorDocente", key = "#evaluacion.docente.id", condition = "#evaluacion.docente != null")
    public EvaluacionDocente crearEvaluacion(EvaluacionDocente evaluacion) {
        return evaluacionDocenteRepository.save(evaluacion);
    }

    @Override
    @Cacheable(value = "evaluacionesPorDocente", key = "#docenteId")
    public List<EvaluacionDocente> obtenerEvaluacionesPorDocente(Long docenteId) {
        Docente docente = docenteRepository.findById(docenteId).orElse(null);
        if (docente == null) return Collections.emptyList();
        return evaluacionDocenteRepository.findByDocente(docente);
    }

    @Override
    @Cacheable(value = "evaluacionDocente", key = "#id")
    public EvaluacionDocente obtenerEvaluacionPorId(Long id) {
        return evaluacionDocenteRepository.findById(id).orElse(null);
    }

    @Override
    @CacheEvict(value = {"evaluacionDocente", "evaluacionesPorDocente"}, allEntries = true)
    public void eliminarEvaluacion(Long id) {
        evaluacionDocenteRepository.deleteById(id);
    }
}
