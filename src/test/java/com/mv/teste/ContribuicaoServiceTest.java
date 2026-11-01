package com.mv.teste;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.mv.teste.dto.ContribuicaoRequest;
import com.mv.teste.exception.NegocioException;
import com.mv.teste.model.Categoria;
import com.mv.teste.model.Contribuicao;
import com.mv.teste.model.Evento;
import com.mv.teste.model.Item;
import com.mv.teste.model.Participante;
import com.mv.teste.repositories.ContribuicaoRepository;
import com.mv.teste.service.ContribuicaoService;
import com.mv.teste.service.EventoService;
import com.mv.teste.service.ItemService;
import com.mv.teste.service.ParticipanteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ContribuicaoServiceTest {

    @Mock ContribuicaoRepository contribuicaoRepository;
    @Mock ItemService itemService;
    @Mock ParticipanteService participanteService;
    @Mock EventoService eventoService;
    @InjectMocks ContribuicaoService service;

    Item item;
    Participante participante;

    @BeforeEach
    void montarCenario() {
        Evento evento = new Evento();
        evento.setId(1L);

        Categoria categoria = new Categoria();
        categoria.setEvento(evento);

        item = new Item();
        item.setId(1L);
        item.setCategoria(categoria);
        item.setQuantidadeNecessaria(10);

        participante = new Participante();
        participante.setId(1L);
        participante.getEventos().add(evento);
    }

    @Test
    void naoPermiteContribuirAcimaDoNecessario() {
        when(itemService.buscar(1L)).thenReturn(item);
        when(participanteService.buscar(1L)).thenReturn(participante);
        when(contribuicaoRepository.somarQuantidadePorItem(1L)).thenReturn(8L);

        NegocioException erro = assertThrows(NegocioException.class,
                () -> service.criar(1L, new ContribuicaoRequest(1L, 5)));

        assertTrue(erro.getMessage().contains("faltam 2"));
    }

    @Test
    void permiteContribuirQuantoFalta() {
        when(itemService.buscar(1L)).thenReturn(item);
        when(participanteService.buscar(1L)).thenReturn(participante);
        when(contribuicaoRepository.somarQuantidadePorItem(1L)).thenReturn(8L);
        when(contribuicaoRepository.save(any(Contribuicao.class))).thenAnswer(i -> i.getArgument(0));

        Contribuicao salva = service.criar(1L, new ContribuicaoRequest(1L, 2));

        assertEquals(2, salva.getQuantidade());
    }

    @Test
    void naoPermiteQuantidadeZero() {
        when(itemService.buscar(1L)).thenReturn(item);
        when(participanteService.buscar(1L)).thenReturn(participante);

        assertThrows(NegocioException.class,
                () -> service.criar(1L, new ContribuicaoRequest(1L, 0)));
    }
}