package com.api.casadoconstrutor.sght.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tb_documentos")
@Data
public class Comprovante {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeArquivo;
    private String tipoArquivo;
    private Long tamanhoArquivo;

    @OneToOne(mappedBy = "comprovante", cascade = CascadeType.ALL)
    @JsonIgnore
    private Solicitacao solicitacao;

}
