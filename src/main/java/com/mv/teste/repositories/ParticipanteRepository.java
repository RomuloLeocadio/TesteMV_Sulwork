package com.mv.teste.repositories;

import com.mv.teste.model.Participante;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipanteRepository extends JpaRepository<Participante, Long> {

    //Aqui é feita a consulta do CPF
    boolean existsByCpf(String cpf);

    boolean existsByCpfAndIdNot(String cpf, Long id);

    List<Participante> findByEventosId(Long eventoId);
}