package com.mv.teste.dto;

import com.mv.teste.model.Evento;
import com.mv.teste.model.StatusEvento;
import java.time.LocalDateTime;

public record EventoResponse(
        Long id,
        String nome,
        String descricao,
        String tipo,
        LocalDateTime dataHora,
        LocalDateTime prazoAlteracoes,
        StatusEvento status) {

    public static EventoResponse de(Evento evt) {
        return new EventoResponse(evt.getId(), evt.getNome(), evt.getDescricao(), evt.getTipo(),
                evt.getDataHora(), evt.getPrazoAlteracoes(), evt.getStatus());
    }
}

