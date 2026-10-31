package com.mv.teste.dto;

import java.time.LocalDateTime;

public record EventoRequest(
        String nome,
        String descricao,
        String tipo,
        LocalDateTime dataHora,
        LocalDateTime prazoAlteracoes) {
}

//Aqui eu envio/crio/edito o nome, descrição e etc do evento