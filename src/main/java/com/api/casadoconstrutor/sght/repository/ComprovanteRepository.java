package com.api.casadoconstrutor.sght.repository;

import com.api.casadoconstrutor.sght.model.Comprovante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComprovanteRepository extends JpaRepository<Comprovante, Long> {

    List<Comprovante> findByNomeArquivoContaining(String nomeArquivo);

}
