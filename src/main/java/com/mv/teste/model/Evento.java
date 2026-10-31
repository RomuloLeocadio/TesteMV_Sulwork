package com.mv.teste.model;


import java.util.HashSet;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.ManyToMany;
import java.util.Set;

import jakarta.persistence.Table;

@Entity 
@Table(name = "tb_evento")
public class Evento {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String titulo;
    private String descricao;
    private String data;

    @ManyToOne
    @JoinColumn (name = "categoria_id")
    private Categoria categoria;

    @ManyToMany 
    @JoinTable(
        name = "tb_evento_participante",
        joinColumns = @JoinColumn(name = "evento_id"),
        inverseJoinColumns = @JoinColumn(name = "participante_id")
    )
    private Set<Participante> participantes = new HashSet<>();

    public Evento(){

    }
    public Evento(Long id, String titulo, Categoria categoria, String descricao, String data) {
        this.id = id;
        this.titulo = titulo;
        this.categoria = categoria;
        this.descricao = descricao;
        this.data = data;
    }

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getTitulo(){
        return titulo;
    }

    public void setTitulo(String titulo){
        this.titulo = titulo;
    }

    public Categoria getCategoria(){
        return categoria;
    }

    public void setCategoria( Categoria categoria){
        this.categoria = categoria;
    }

    public String getDescricao(){
        return descricao;
    }

    public void setDescricao(String descricao){
        this.descricao = descricao;
    }

    public String getData(){
        return data;
    }

    public void setData(String data){
        this.data = data;
    }

    public Set<Participante> getParticipantes(){
        return participantes;
    }
}
