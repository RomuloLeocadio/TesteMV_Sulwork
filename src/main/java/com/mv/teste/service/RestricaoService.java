package com.mv.teste.service;

import com.mv.teste.dto.RestricaoRequest;
import com.mv.teste.exception.NegocioException;
import com.mv.teste.exception.RecursoNaoEncontradoException;
import com.mv.teste.model.Alergeno;
import com.mv.teste.model.Participante;
import com.mv.teste.model.RestricaoAlimentar;
import com.mv.teste.repositories.AlergenoRepository;
import com.mv.teste.repositories.RestricaoAlimentarRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class RestricaoService {

    private final RestricaoAlimentarRepository restricaoRepository;
    private final AlergenoRepository alergenoRepository;
    private final ParticipanteService participanteService;

    public RestricaoService(RestricaoAlimentarRepository restricaoRepository,
                            AlergenoRepository alergenoRepository,
                            ParticipanteService participanteService) {
        this.restricaoRepository = restricaoRepository;
        this.alergenoRepository = alergenoRepository;
        this.participanteService = participanteService;
    }

    public RestricaoAlimentar criar(Long participanteId, RestricaoRequest req) {
        Participante participante = participanteService.buscar(participanteId);
        if (req.tipo() == null || req.tipo().isBlank()) {
            throw new NegocioException("O tipo da restrição é obrigatório.");
        }

        Set<Alergeno> alergenos = new HashSet<>();
        if (req.alergenoIds() != null && !req.alergenoIds().isEmpty()) {
            List<Alergeno> encontrados = alergenoRepository.findAllById(req.alergenoIds());
            if (encontrados.size() != req.alergenoIds().size()) {
                throw new NegocioException("Algum alérgeno informado não existe.");
            }
            alergenos.addAll(encontrados);
        }

        RestricaoAlimentar restricao = new RestricaoAlimentar();
        restricao.setParticipante(participante);
        restricao.setTipo(req.tipo().trim());
        restricao.setObservacoes(req.observacoes());
        restricao.setAlergenos(alergenos);
        return restricaoRepository.save(restricao);
    }

    public List<RestricaoAlimentar> listarPorParticipante(Long participanteId) {
        participanteService.buscar(participanteId);
        return restricaoRepository.findByParticipanteId(participanteId);
    }

    public void excluir(Long id) {
        RestricaoAlimentar restricao = restricaoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Restrição " + id + " não encontrada."));
        restricaoRepository.delete(restricao);
    }
}