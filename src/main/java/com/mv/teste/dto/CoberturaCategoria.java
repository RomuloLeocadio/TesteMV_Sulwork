package com.mv.teste.dto;

public record CoberturaCategoria(
        Long categoriaId,
        String nome,
        Integer meta,
        long totalNecessario,
        long totalContribuido,
        boolean metaAtingida,
        double percentual) {
}