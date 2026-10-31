package com.mv.teste.service;

import com.mv.teste.dto.EventoRequest;
import com.mv.teste.exception.NegocioException;
import com.mv.teste.exception.RecursoNaoEncontradoException;
import com.mv.teste.model.Evento;
import com.mv.teste.model.StatusEvento;
import com.mv.teste.repositories.EventoRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EventoService {

    private final EventoRepository eventoRepository;

    public EventoService(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }

    @Transactional
    public Evento criar(EventoRequest req) {
        Evento evento = new Evento();
        preencher(evento, req);
        evento.setStatus(StatusEvento.RASCUNHO);
        return eventoRepository.save(evento);
    }

    public List<Evento> listar() {
        return eventoRepository.findAll();
    }

    public Evento buscar(Long id) {
        return eventoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Evento " + id + " não encontrado."));
    }

    @Transactional
    public Evento atualizar(Long id, EventoRequest req) {
        Evento evento = buscar(id);
        if (evento.getStatus() != StatusEvento.RASCUNHO && evento.getStatus() != StatusEvento.ABERTO) {
            throw new NegocioException("Só é possível editar eventos em RASCUNHO ou ABERTO.");
        }
        preencher(evento, req);
        return eventoRepository.save(evento);
    }

    @Transactional
    public Evento alterarStatus(Long id, StatusEvento novo) {
        Evento evento = buscar(id);
        if (!evento.getStatus().podeIrPara(novo)) {
            throw new NegocioException("Transição inválida: " + evento.getStatus() + " -> " + novo);
        }
        evento.setStatus(novo);
        return eventoRepository.save(evento);
    }

    @Transactional
    public void excluir(Long id) {
        Evento evento = buscar(id);
        if (evento.getStatus() != StatusEvento.RASCUNHO) {
            throw new NegocioException("Só é possível excluir eventos em RASCUNHO. Use CANCELADO.");
        }
        eventoRepository.delete(evento);
    }

    public void exigirEditavel(Evento evento) {
        if (evento.getStatus() != StatusEvento.RASCUNHO && evento.getStatus() != StatusEvento.ABERTO) {
            throw new NegocioException("O evento está " + evento.getStatus()
                    + " e não aceita alterações em categorias e itens.");
        }
    }
    
    private void preencher(Evento evento, EventoRequest req) {
        if (req.nome() == null || req.nome().isBlank()) {
            throw new NegocioException("O nome do evento é obrigatório.");
        }
        if (req.dataHora() == null || req.prazoAlteracoes() == null) {
            throw new NegocioException("Data/hora e prazo para alterações são obrigatórios.");
        }
        if (req.prazoAlteracoes().isAfter(req.dataHora())) {
            throw new NegocioException("O prazo para alterações não pode ser depois da data do evento.");
        }
        evento.setNome(req.nome().trim());
        evento.setDescricao(req.descricao());
        evento.setTipo(req.tipo());
        evento.setDataHora(req.dataHora());
        evento.setPrazoAlteracoes(req.prazoAlteracoes());
    }
}