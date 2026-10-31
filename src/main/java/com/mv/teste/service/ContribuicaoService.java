package com.mv.teste.service;

import com.mv.teste.dto.ContribuicaoRequest;
import com.mv.teste.exception.NegocioException;
import com.mv.teste.exception.RecursoNaoEncontradoException;
import com.mv.teste.model.Contribuicao;
import com.mv.teste.model.Evento;
import com.mv.teste.model.Item;
import com.mv.teste.model.Participante;
import com.mv.teste.repositories.ContribuicaoRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContribuicaoService {

    private final ContribuicaoRepository contribuicaoRepository;
    private final ItemService itemService;
    private final ParticipanteService participanteService;
    private final EventoService eventoService;

    public ContribuicaoService(ContribuicaoRepository contribuicaoRepository, ItemService itemService,
                               ParticipanteService participanteService, EventoService eventoService) {
        this.contribuicaoRepository = contribuicaoRepository;
        this.itemService = itemService;
        this.participanteService = participanteService;
        this.eventoService = eventoService;
    }

    @Transactional
    public Contribuicao criar(Long itemId, ContribuicaoRequest req) {
        Item item = itemService.buscar(itemId);
        Evento evento = item.getCategoria().getEvento();
        eventoService.exigirAceitaContribuicoes(evento);

        if (req.participanteId() == null) {
            throw new NegocioException("O participante é obrigatório.");
        }
        Participante participante = participanteService.buscar(req.participanteId());
        if (!participante.getEventos().contains(evento)) {
            throw new NegocioException("O participante não está inscrito neste evento.");
        }

        validarQuantidade(item, req.quantidade(), 0);

        Contribuicao contribuicao = new Contribuicao();
        contribuicao.setItem(item);
        contribuicao.setParticipante(participante);
        contribuicao.setQuantidade(req.quantidade());
        return contribuicaoRepository.save(contribuicao);
    }

    public List<Contribuicao> listarPorItem(Long itemId) {
        itemService.buscar(itemId);
        return contribuicaoRepository.findByItemId(itemId);
    }

    public Contribuicao buscar(Long id) {
        return contribuicaoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Contribuição " + id + " não encontrada."));
    }

    @Transactional
    public Contribuicao atualizar(Long id, ContribuicaoRequest req) {
        Contribuicao contribuicao = buscar(id);
        eventoService.exigirAceitaContribuicoes(contribuicao.getItem().getCategoria().getEvento());

        validarQuantidade(contribuicao.getItem(), req.quantidade(), contribuicao.getQuantidade());

        contribuicao.setQuantidade(req.quantidade());
        return contribuicaoRepository.save(contribuicao);
    }

    @Transactional
    public void excluir(Long id) {
        Contribuicao contribuicao = buscar(id);
        eventoService.exigirAceitaContribuicoes(contribuicao.getItem().getCategoria().getEvento());
        contribuicaoRepository.delete(contribuicao);
    }

    private void validarQuantidade(Item item, Integer quantidade, int quantidadeAntiga) {
        if (quantidade == null || quantidade <= 0) {
            throw new NegocioException("A quantidade deve ser maior que zero.");
        }
        long jaContribuido = contribuicaoRepository.somarQuantidadePorItem(item.getId());
        long faltam = item.getQuantidadeNecessaria() - (jaContribuido - quantidadeAntiga);
        if (quantidade > faltam) {
            throw new NegocioException("Quantidade acima do necessário. Ainda faltam " + faltam
                    + " unidade(s) para este item.");
        }
    }
}