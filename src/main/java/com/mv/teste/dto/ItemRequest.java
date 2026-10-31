package com.mv.teste.dto;

import java.util.Set;

public record ItemRequest(
    String nome,
    String descricao,
    Integer quantidadeNecessaria,
    boolean obrigatorio,
    Set<Long> alergenoIds) {

    
}
