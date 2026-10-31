package com.mv.teste.repositories;

import com.mv.teste.model.Contribuicao;
import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;

//@Repository
public interface ContribuicaoRepository extends JpaRepository<Contribuicao, Long> {
}