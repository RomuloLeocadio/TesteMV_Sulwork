package com.mv.teste;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mv.teste.model.StatusEvento;
import org.junit.jupiter.api.Test;

class StatusEventoTest {

    @Test
    void rascunhoPodeSerAberto() {
        assertTrue(StatusEvento.RASCUNHO.podeIrPara(StatusEvento.ABERTO));
    }

    @Test
    void abertoNaoPodeVoltarParaRascunho() {
        assertFalse(StatusEvento.ABERTO.podeIrPara(StatusEvento.RASCUNHO));
    }

    @Test
    void realizadoECanceladoSaoStatusFinais() {
        for (StatusEvento destino : StatusEvento.values()) {
            assertFalse(StatusEvento.REALIZADO.podeIrPara(destino));
            assertFalse(StatusEvento.CANCELADO.podeIrPara(destino));
        }
    }
}