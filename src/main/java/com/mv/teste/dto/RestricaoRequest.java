package com.mv.teste.dto;

import java.util.Set;

public record RestricaoRequest(String tipo, String observacoes, Set<Long> alergenoIds) {
}