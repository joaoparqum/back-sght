package com.api.casadoconstrutor.sght.repository;

import com.api.casadoconstrutor.sght.model.Solicitacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolicitacaoRepository extends JpaRepository<Solicitacao, Long> {

    // Recupera solicitações de um usuário específico
    @Query("SELECT s FROM Solicitacao s WHERE s.user.id = :userId")
    List<Solicitacao> findByUserId(@Param("userId") String userId);

    // Recupera todas as solicitações (para ADMINs)
    List<Solicitacao> findAll();

}
