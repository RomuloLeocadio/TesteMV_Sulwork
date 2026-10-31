package com.mv.teste.dto;

import com.mv.teste.model.RestricaoAlimentar;
import java.util.List;

public record RestricaoResponse (
    long id,
    String tipo,
    String observacoes,
    List<AlergenoResponse> alergenos) {

        public static RestricaoResponse de (RestricaoAlimentar ra) {
            return new RestricaoResponse(ra.getId(), ra.getTipo(), ra.getObservacoes(),
            ra.getAlergenos().stream().map(AlergenoResponse::de).toList());
        }
    
}