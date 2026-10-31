package com.mv.teste.service;

import com.mv.teste.dto.CoberturaCategoria;
import com.mv.teste.dto.CoberturaEvento;
import com.mv.teste.model.Categoria;
import com.mv.teste.model.Item;
import com.mv.teste.repositories.CategoriaRepository;
import com.mv.teste.repositories.ContribuicaoRepository;
import com.mv.teste.repositories.ItemRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CoberturaService {

    private final CategoriaRepository categoriaRepository;
    private final ItemRepository itemRepository;
    private final ContribuicaoRepository contribuicaoRepository;
    private final EventoService eventoService;

    public CoberturaService(CategoriaRepository categoriaRepository, ItemRepository itemRepository,
                            ContribuicaoRepository contribuicaoRepository, EventoService eventoService) {
        this.categoriaRepository = categoriaRepository;
        this.itemRepository = itemRepository;
        this.contribuicaoRepository = contribuicaoRepository;
        this.eventoService = eventoService;
    }

    public CoberturaEvento calcular(Long eventoId) {
        eventoService.buscar(eventoId);

        long necessarioEvento = 0;
        long contribuidoEvento = 0;
        List<String> pendentes = new ArrayList<>();
        List<CoberturaCategoria> resultado = new ArrayList<>();

        for (Categoria categoria : categoriaRepository.findByEventoId(eventoId)) {
            long necessarioCategoria = 0;
            long contribuidoCategoria = 0;

            for (Item item : itemRepository.findByCategoriaId(categoria.getId())) {
                long contribuido = contribuicaoRepository.somarQuantidadePorItem(item.getId());
                necessarioCategoria += item.getQuantidadeNecessaria();
                contribuidoCategoria += contribuido;

                if (item.isObrigatorio() && contribuido < item.getQuantidadeNecessaria()) {
                    pendentes.add(item.getNome());
                }
            }

            boolean metaAtingida = categoria.getMeta() != null && contribuidoCategoria >= categoria.getMeta();
            resultado.add(new CoberturaCategoria(categoria.getId(), categoria.getNome(), categoria.getMeta(),
                    necessarioCategoria, contribuidoCategoria, metaAtingida,
                    percentual(contribuidoCategoria, necessarioCategoria)));

            necessarioEvento += necessarioCategoria;
            contribuidoEvento += contribuidoCategoria;
        }

        return new CoberturaEvento(eventoId, necessarioEvento, contribuidoEvento,
                percentual(contribuidoEvento, necessarioEvento), pendentes, resultado);
    }

    private double percentual(long contribuido, long necessario) {
        if (necessario == 0) {
            return 0;
        }
        return Math.round(contribuido * 1000.0 / necessario) / 10.0;
    }
}