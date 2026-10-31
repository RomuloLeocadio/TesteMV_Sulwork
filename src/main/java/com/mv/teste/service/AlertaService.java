package com.mv.teste.service;

import com.mv.teste.dto.AlertaAlergeno;
import com.mv.teste.model.Alergeno;
import com.mv.teste.model.Categoria;
import com.mv.teste.model.Item;
import com.mv.teste.model.Participante;
import com.mv.teste.model.RestricaoAlimentar;
import com.mv.teste.repositories.CategoriaRepository;
import com.mv.teste.repositories.ItemRepository;
import com.mv.teste.repositories.ParticipanteRepository;
import com.mv.teste.repositories.RestricaoAlimentarRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AlertaService {

    private final CategoriaRepository categoriaRepository;
    private final ItemRepository itemRepository;
    private final ParticipanteRepository participanteRepository;
    private final RestricaoAlimentarRepository restricaoRepository;
    private final EventoService eventoService;

    public AlertaService(CategoriaRepository categoriaRepository, ItemRepository itemRepository,
                         ParticipanteRepository participanteRepository,
                         RestricaoAlimentarRepository restricaoRepository, EventoService eventoService) {
        this.categoriaRepository = categoriaRepository;
        this.itemRepository = itemRepository;
        this.participanteRepository = participanteRepository;
        this.restricaoRepository = restricaoRepository;
        this.eventoService = eventoService;
    }

    @Transactional(readOnly = true)
    public List<AlertaAlergeno> calcular(Long eventoId) {
        eventoService.buscar(eventoId);
        List<Participante> inscritos = participanteRepository.findByEventosId(eventoId);
        List<AlertaAlergeno> alertas = new ArrayList<>();

        for (Categoria categoria : categoriaRepository.findByEventoId(eventoId)) {
            for (Item item : itemRepository.findByCategoriaId(categoria.getId())) {

                Set<Long> idsDoItem = new HashSet<>();
                List<String> nomesDoItem = new ArrayList<>();
                for (Alergeno a : item.getAlergenos()) {
                    idsDoItem.add(a.getId());
                    nomesDoItem.add(a.getNome());
                }

                List<String> afetados = new ArrayList<>();
                for (Participante p : inscritos) {
                    if (temRestricao(p, idsDoItem)) {
                        afetados.add(p.getNome());
                    }
                }

                if (!afetados.isEmpty()) {
                    alertas.add(new AlertaAlergeno(item.getId(), item.getNome(), nomesDoItem, afetados));
                }
            }
        }
        return alertas;
    }

    private boolean temRestricao(Participante participante, Set<Long> idsAlergenosDoItem) {
        for (RestricaoAlimentar r : restricaoRepository.findByParticipanteId(participante.getId())) {
            for (Alergeno a : r.getAlergenos()) {
                if (idsAlergenosDoItem.contains(a.getId())) {
                    return true;
                }
            }
        }
        return false;
    }
}