package com.mv.teste.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tb_restricao_alimentar")
public class RestricaoAlimentar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipo; // Ex: Vegano, Celíaco, Intolerante à lactose
    private String observacoes;

    @ManyToOne
    @JoinColumn(name = "participante_id")
    private Participante participante;

    @ManyToMany
    @JoinTable(
        name = "tb_restricao_alergeno",
        joinColumns = @JoinColumn(name = "restricao_id"),
        inverseJoinColumns = @JoinColumn(name = "alergeno_id")
    )
    private Set<Alergeno> alergenos = new HashSet<>();

    public RestricaoAlimentar() {
    }

    public RestricaoAlimentar(Long id, String tipo, String observacoes, Participante participante) {
        this.id = id;
        this.tipo = tipo;
        this.observacoes = observacoes;
        this.participante = participante;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Participante getParticipante() {
        return participante;
    }

    public void setParticipante(Participante participante) {
        this.participante = participante;
    }

    public Set<Alergeno> getAlergenos() {
        return alergenos;
    }

    public void setAlergenos(Set<Alergeno> alergenos) {
        this.alergenos = alergenos;
    }
    
}
