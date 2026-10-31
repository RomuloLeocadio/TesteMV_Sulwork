package com.mv.teste.dto;

import java.util.List;

public record AlertaAlergeno(
    Long itemId,
    String itemNome,
    List<String> alergenos,
    List<String> participantesAfetados){
}
