package com.mv.teste;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.mv.teste.dto.ParticipanteRequest;
import com.mv.teste.exception.NegocioException;
import com.mv.teste.model.Participante;
import com.mv.teste.repositories.ParticipanteRepository;
import com.mv.teste.service.EventoService;
import com.mv.teste.service.ParticipanteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ParticipanteServiceTest {

    @Mock ParticipanteRepository participanteRepository;
    @Mock EventoService eventoService;
    @InjectMocks ParticipanteService service;

    @Test
    void rejeitaCpfInvalido() {
        assertThrows(NegocioException.class,
                () -> service.criar(new ParticipanteRequest("Joao", "j@teste.com", "111.111.111-11")));
    }

    @Test
    void aceitaCpfValidoEGuardaSemFormatacao() {
        when(participanteRepository.save(any(Participante.class))).thenAnswer(i -> i.getArgument(0));

        Participante salvo = service.criar(new ParticipanteRequest("Maria", "m@teste.com", "529.982.247-25"));

        assertEquals("52998224725", salvo.getCpf());
    }

    @Test
    void rejeitaCpfDuplicado() {
        when(participanteRepository.existsByCpf("52998224725")).thenReturn(true);

        assertThrows(NegocioException.class,
                () -> service.criar(new ParticipanteRequest("Maria", "m@teste.com", "529.982.247-25")));
    }
}