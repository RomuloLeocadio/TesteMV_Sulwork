package com.mv.teste.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.HashSet;
import java.util.Set;

@Entity 
@Table(name = "tb_participante")
public class Participante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String email;

    @ManyToMany(mappedBy = "participantes")
    private Set<Evento> eventos = new HashSet<>();

    @OneToMany(mappedBy = "participante", cascade = CascadeType.ALL)
private Set<RestricaoAlimentar> restricoesAlimentares = new HashSet<>();

    public Set<RestricaoAlimentar> getRestricoesAlimentares() {
        return restricoesAlimentares;
    }

    public void setRestricoesAlimentares(Set<RestricaoAlimentar> restricoesAlimentares) {
        this.restricoesAlimentares = restricoesAlimentares;
    }

    public Participante() {
    }

    public Participante(Long id, String nome, String email){
        this.id = id;
        this.nome = nome;
        this.email = email;
    }

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getNome(){
        return nome;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public String getEmail(){
        return email;
    }

    public void SetEmail(String email){
        this.email = email;
    }

    public Set<Evento> getEventos() {
        return eventos;
    }
    
}
