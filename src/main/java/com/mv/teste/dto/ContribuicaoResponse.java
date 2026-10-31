package com.mv.teste.dto;

import com.mv.teste.model.Contribuicao;

public record ContribuicaoResponse(
        Long id,
        Long itemId,
        String itemNome,
        Long participanteId,
        String participanteNome,
        Integer quantidade) {

    public static ContribuicaoResponse de(Contribuicao ctb) {
        return new ContribuicaoResponse(ctb.getId(), ctb.getItem().getId(), ctb.getItem().getNome(),
                ctb.getParticipante().getId(), ctb.getParticipante().getNome(), ctb.getQuantidade());
    }
}