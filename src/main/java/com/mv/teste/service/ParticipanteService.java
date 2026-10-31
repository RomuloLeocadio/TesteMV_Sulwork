package com.mv.teste.service;

import com.mv.teste.dto.ParticipanteRequest;
import com.mv.teste.exception.NegocioException;
import com.mv.teste.exception.RecursoNaoEncontradoException;
import com.mv.teste.model.Evento;
import com.mv.teste.model.Participante;
import com.mv.teste.model.StatusEvento;
import com.mv.teste.repositories.ParticipanteRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ParticipanteService {

    private final ParticipanteRepository participanteRepository;
    private final EventoService eventoService;

    public ParticipanteService(ParticipanteRepository participanteRepository, EventoService eventoService) {
        this.participanteRepository = participanteRepository;
        this.eventoService = eventoService;
    }

    @Transactional
    public Participante criar(ParticipanteRequest req) {
        String cpf = normalizarCpf(req.cpf());
        if (participanteRepository.existsByCpf(cpf)) {
            throw new NegocioException("Já existe participante com este CPF.");
        }
        Participante p = new Participante();
        preencher(p, req, cpf);
        return participanteRepository.save(p);
    }

    public List<Participante> listar() {
        return participanteRepository.findAll();
    }

    public Participante buscar(Long id) {
        return participanteRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Participante " + id + " não encontrado."));
    }

    @Transactional
    public Participante atualizar(Long id, ParticipanteRequest req) {
        Participante p = buscar(id);
        String cpf = normalizarCpf(req.cpf());
        if (participanteRepository.existsByCpfAndIdNot(cpf, id)) {
            throw new NegocioException("Já existe outro participante com este CPF.");
        }
        preencher(p, req, cpf);
        return participanteRepository.save(p);
    }

    @Transactional
    public void excluir(Long id) {
        participanteRepository.delete(buscar(id));
    }

    @Transactional
    public void inscrever(Long eventoId, Long participanteId) {
        Evento evento = eventoService.buscar(eventoId);
        Participante p = buscar(participanteId);
        if (evento.getStatus() != StatusEvento.ABERTO) {
            throw new NegocioException("Só é possível inscrever participantes em eventos ABERTOS.");
        }
        if (p.getEventos().contains(evento)) {
            throw new NegocioException("Participante já inscrito neste evento.");
        }
        p.getEventos().add(evento);
        participanteRepository.save(p);
    }

    public List<Participante> listarPorEvento(Long eventoId) {
        eventoService.buscar(eventoId);
        return participanteRepository.findByEventosId(eventoId);
    }

    private void preencher(Participante p, ParticipanteRequest req, String cpf) {
        if (req.nome() == null || req.nome().isBlank()) {
            throw new NegocioException("O nome do participante é obrigatório.");
        }
        p.setNome(req.nome().trim());
        p.setEmail(req.email());
        p.setCpf(cpf);
    }

    private String normalizarCpf(String cpf) {
        if (cpf == null) {
            throw new NegocioException("O CPF é obrigatório.");
        }
        String limpo = cpf.replaceAll("\\D", "");
        if (!cpfValido(limpo)) {
            throw new NegocioException("CPF inválido.");
        }
        return limpo;
    }

    private boolean cpfValido(String cpf) {
        if (cpf.length() != 11 || cpf.chars().distinct().count() == 1) {
            return false;
        }
        for (int t = 9; t < 11; t++) {
            int soma = 0;
            for (int i = 0; i < t; i++) {
                soma += (cpf.charAt(i) - '0') * (t + 1 - i);
            }
            int dv = (soma * 10) % 11;
            if (dv == 10) {
                dv = 0;
            }
            if (dv != cpf.charAt(t) - '0') {
                return false;
            }
        }
        return true;
    }
}