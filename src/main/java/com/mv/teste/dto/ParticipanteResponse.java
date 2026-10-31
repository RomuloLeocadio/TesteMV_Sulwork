package com.mv.teste.dto;

import com.mv.teste.model.Participante;

public record ParticipanteResponse(Long id, String nome, String email, String cpf) {

    public static ParticipanteResponse de(Participante partc) {
        return new ParticipanteResponse(partc.getId(), partc.getNome(), partc.getEmail(), partc.getCpf());
    }
}