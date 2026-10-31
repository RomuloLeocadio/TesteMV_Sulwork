package com.mv.teste.dto;

import com.mv.teste.model.Alergeno;

public record AlergenoResponse(Long id, String nome, String descricao) {

    public static AlergenoResponse de(Alergeno alg) {
        return new AlergenoResponse(alg.getId(), alg.getNome(), alg.getDescricao());
    }
}