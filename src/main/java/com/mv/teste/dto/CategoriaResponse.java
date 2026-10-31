package com.mv.teste.dto;

import com.mv.teste.model.Categoria;



public record CategoriaResponse (Long id, String nome, Integer meta, Long eventoID){
    
    public static CategoriaResponse de(Categoria ctg){
        return new CategoriaResponse(ctg.getId(), ctg.getNome(), ctg.getMeta(), ctg.getEvento().getId());
    }
}
