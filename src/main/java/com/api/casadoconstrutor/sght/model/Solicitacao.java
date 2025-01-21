package com.api.casadoconstrutor.sght.model;

import com.api.casadoconstrutor.sght.enuns.StatusSolicitacao;
import com.api.casadoconstrutor.sght.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_solicitacoes")
@Data
public class Solicitacao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime data;
    private String motivo;
    private int horasSolicitadas;
    private StatusSolicitacao status;
    private boolean visto = false;

    @OneToOne(cascade = CascadeType.ALL)
    private Comprovante comprovante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;


}
