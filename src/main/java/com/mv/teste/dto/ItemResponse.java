package com.mv.teste.dto;

import com.mv.teste.model.Item;
import java.util.List;

public record ItemResponse(
    Long id,
    String nome,
    String descricao,
    Integer quantidadeNecessaria,
    boolean obrigatorio,
    Long categoriaId,
    List<AlergenoResponse> alergenos) {

        public static ItemResponse de(Item it){
            return new ItemResponse(it.getId(), it.getNome(), it.getDescricao(), it.getQuantidadeNecessaria(), it.isObrigatorio(), 
        it.getCategoria().getId(), it.getAlergenos().stream().map(AlergenoResponse::de).toList());
        }
}
