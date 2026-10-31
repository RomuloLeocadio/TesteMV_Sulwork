package com.mv.teste.repositories;

import com.mv.teste.model.Contribuicao;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ContribuicaoRepository extends JpaRepository<Contribuicao, Long> {

    List<Contribuicao> findByItemId(Long itemId);

    @Query(nativeQuery = true,
           value = "SELECT COALESCE(SUM(quantidade), 0) FROM tb_contribuicao WHERE item_id = :itemId")
    Long somarQuantidadePorItem(@Param("itemId") Long itemId);
}