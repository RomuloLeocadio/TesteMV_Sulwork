package com.mv.teste.service;

import com.mv.teste.dto.AlergenoRequest;
import com.mv.teste.exception.NegocioException;
import com.mv.teste.model.Alergeno;
import com.mv.teste.repositories.AlergenoRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AlergenoService {

    private final AlergenoRepository alergenoRepository;

    public AlergenoService(AlergenoRepository alergenoRepository) {
        this.alergenoRepository = alergenoRepository;
    }

    public Alergeno criar(AlergenoRequest req) {
        if (req.nome() == null || req.nome().isBlank()) {
            throw new NegocioException("O nome do alérgeno é obrigatório.");
        }
        Alergeno alergeno = new Alergeno();
        alergeno.setNome(req.nome().trim());
        alergeno.setDescricao(req.descricao());
        return alergenoRepository.save(alergeno);
    }

    public List<Alergeno> listar() {
        return alergenoRepository.findAll();
    }
}