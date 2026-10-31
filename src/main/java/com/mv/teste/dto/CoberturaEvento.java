package com.mv.teste.dto;

import java.util.List;

public record CoberturaEvento(
        Long eventoId,
        long totalNecessario,
        long totalContribuido,
        double percentual,
        List<String> obrigatoriosPendentes,
        List<CoberturaCategoria> categorias) {
}