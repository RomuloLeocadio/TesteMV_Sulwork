package com.mv.teste.service;

import com.mv.teste.dto.CategoriaRequest;
import com.mv.teste.exception.NegocioException;
import com.mv.teste.exception.RecursoNaoEncontradoException;
import com.mv.teste.model.Categoria;
import com.mv.teste.model.Evento;
import com.mv.teste.repositories.CategoriaRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final EventoService eventoService;

    public CategoriaService(CategoriaRepository categoriaRepository, EventoService eventoService) {
        this.categoriaRepository = categoriaRepository;
        this.eventoService = eventoService;
    }

    @Transactional
    public Categoria criar(Long eventoId, CategoriaRequest req) {
        Evento evento = eventoService.buscar(eventoId);
        eventoService.exigirEditavel(evento);
        Categoria categoria = new Categoria();
        categoria.setEvento(evento);
        preencher(categoria, req);
        return categoriaRepository.save(categoria);
    }

    public List<Categoria> listarPorEvento(Long eventoId) {
        eventoService.buscar(eventoId);
        return categoriaRepository.findByEventoId(eventoId);
    }

    public Categoria buscar(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Categoria " + id + " não encontrada."));
    }

    @Transactional
    public Categoria atualizar(Long id, CategoriaRequest req) {
        Categoria categoria = buscar(id);
        eventoService.exigirEditavel(categoria.getEvento());
        preencher(categoria, req);
        return categoriaRepository.save(categoria);
    }

    @Transactional
    public void excluir(Long id) {
        Categoria categoria = buscar(id);
        eventoService.exigirEditavel(categoria.getEvento());
        if (!categoria.getItens().isEmpty()) {
            throw new NegocioException("Não é possível excluir uma categoria que possui itens.");
        }
        categoriaRepository.delete(categoria);
    }

    private void preencher(Categoria categoria, CategoriaRequest req) {
        if (req.nome() == null || req.nome().isBlank()) {
            throw new NegocioException("O nome da categoria é obrigatório.");
        }
        if (req.meta() != null && req.meta() < 0) {
            throw new NegocioException("A meta da categoria não pode ser negativa.");
        }
        categoria.setNome(req.nome().trim());
        categoria.setMeta(req.meta());
    }
}