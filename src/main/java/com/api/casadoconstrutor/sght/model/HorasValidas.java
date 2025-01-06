package com.api.casadoconstrutor.sght.model;

import com.api.casadoconstrutor.sght.enuns.Filial;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@Table(name = "tb_horas")
@Data
public class HorasValidas implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeColaborador;

    @Enumerated(EnumType.STRING)
    private Filial filial;

    private String junhoJulho;
    private String agosto;
    private String setembroOutubro;
    private String novembro;
    private String dezembro;
    private String janeiro;
    private String fevereiro;
    private String marco;
    private String abril;
    private String maio;
    private String junho;


}
