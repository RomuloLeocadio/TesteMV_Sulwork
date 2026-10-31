package com.mv.teste.service;

import com.mv.teste.model.Categoria;
import com.mv.teste.repositories.CategoriaRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    private CategoriaRepository repository;

    @Transactional(readOnly = true)
    public List<Categoria> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Categoria findById(Long id) {
        Optional<Categoria> obj = repository.findById(id);
        return obj.orElseThrow(() -> new RuntimeException("Categoria não encontrada com o ID: " + id));
    }

    @Transactional
    public Categoria insert(Categoria categoria) {
        return repository.save(categoria);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Categoria não encontrada para exclusão.");
        }
        repository.deleteById(id);
    }
}