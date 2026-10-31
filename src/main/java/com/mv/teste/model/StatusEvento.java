package com.mv.teste.model;

public enum StatusEvento {
    RASCUNHO, ABERTO, ENCERRADO, REALIZADO, CANCELADO;

    public boolean podeIrPara(StatusEvento destino) {
        return switch (this) {
            case RASCUNHO -> destino == ABERTO || destino == CANCELADO;
            case ABERTO -> destino == ENCERRADO || destino == CANCELADO;
            case ENCERRADO -> destino == REALIZADO || destino == CANCELADO;
            case REALIZADO, CANCELADO -> false;
        };
    }
}