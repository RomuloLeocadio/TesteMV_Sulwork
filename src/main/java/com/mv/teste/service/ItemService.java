package com.mv.teste.service;

import com.mv.teste.dto.ItemRequest;
import com.mv.teste.exception.NegocioException;
import com.mv.teste.exception.RecursoNaoEncontradoException;
import com.mv.teste.model.Alergeno;
import com.mv.teste.model.Categoria;
import com.mv.teste.model.Contribuicao;
import com.mv.teste.model.Item;
import com.mv.teste.repositories.AlergenoRepository;
import com.mv.teste.repositories.ItemRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final AlergenoRepository alergenoRepository;
    private final CategoriaService categoriaService;
    private final EventoService eventoService;

    public ItemService(ItemRepository itemRepository, AlergenoRepository alergenoRepository,
                       CategoriaService categoriaService, EventoService eventoService) {
        this.itemRepository = itemRepository;
        this.alergenoRepository = alergenoRepository;
        this.categoriaService = categoriaService;
        this.eventoService = eventoService;
    }

    @Transactional
    public Item criar(Long categoriaId, ItemRequest req) {
        Categoria categoria = categoriaService.buscar(categoriaId);
        eventoService.exigirEditavel(categoria.getEvento());
        Item item = new Item();
        item.setCategoria(categoria);
        preencher(item, req);
        return itemRepository.save(item);
    }

    public List<Item> listarPorCategoria(Long categoriaId) {
        categoriaService.buscar(categoriaId);
        return itemRepository.findByCategoriaId(categoriaId);
    }

    public Item buscar(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Item " + id + " não encontrado."));
    }

    @Transactional
    public Item atualizar(Long id, ItemRequest req) {
        Item item = buscar(id);
        eventoService.exigirEditavel(item.getCategoria().getEvento());
        int jaContribuido = item.getContribuicoes().stream().mapToInt(Contribuicao::getQuantidade).sum();
        if (req.quantidadeNecessaria() != null && req.quantidadeNecessaria() < jaContribuido) {
            throw new NegocioException("A quantidade necessária não pode ser menor que o já contribuído ("
                    + jaContribuido + ").");
        }
        preencher(item, req);
        return itemRepository.save(item);
    }

    @Transactional
    public void excluir(Long id) {
        Item item = buscar(id);
        eventoService.exigirEditavel(item.getCategoria().getEvento());
        if (!item.getContribuicoes().isEmpty()) {
            throw new NegocioException("Não é possível excluir um item que já possui contribuições.");
        }
        itemRepository.delete(item);
    }

    private void preencher(Item item, ItemRequest req) {
        if (req.nome() == null || req.nome().isBlank()) {
            throw new NegocioException("O nome do item é obrigatório.");
        }
        if (req.quantidadeNecessaria() == null || req.quantidadeNecessaria() <= 0) {
            throw new NegocioException("A quantidade necessária deve ser maior que zero.");
        }
        item.setNome(req.nome().trim());
        item.setDescricao(req.descricao());
        item.setQuantidadeNecessaria(req.quantidadeNecessaria());
        item.setObrigatorio(req.obrigatorio());
        item.setAlergenos(buscarAlergenos(req.alergenoIds()));
    }

    private Set<Alergeno> buscarAlergenos(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return new HashSet<>();
        }
        List<Alergeno> encontrados = alergenoRepository.findAllById(ids);
        if (encontrados.size() != ids.size()) {
            throw new NegocioException("Algum alérgeno informado não existe.");
        }
        return new HashSet<>(encontrados);
    }
}