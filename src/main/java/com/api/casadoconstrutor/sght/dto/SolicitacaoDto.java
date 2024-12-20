package com.api.casadoconstrutor.sght.dto;

import com.api.casadoconstrutor.sght.enuns.StatusSolicitacao;
import com.api.casadoconstrutor.sght.model.Solicitacao;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record SolicitacaoDto(
        Long id,
        @NotNull LocalDateTime data,
        @NotNull String motivo,
        int horasSolicitadas,
        @NotNull String status
) {
    // Converte uma entidade Solicitacao para SolicitacaoDTO
    public static SolicitacaoDto fromEntity(Solicitacao solicitacao) {
        return new SolicitacaoDto(
                solicitacao.getId(),
                solicitacao.getData(),
                solicitacao.getMotivo(),
                solicitacao.getHorasSolicitadas(),
                solicitacao.getStatus().name() // Converte o enum para String
        );
    }

    // Converte um DTO para uma entidade Solicitacao
    public static Solicitacao toEntity(SolicitacaoDto dto) {
        Solicitacao solicitacao = new Solicitacao();
        solicitacao.setId(dto.id());
        solicitacao.setData(dto.data());
        solicitacao.setMotivo(dto.motivo());
        solicitacao.setHorasSolicitadas(dto.horasSolicitadas());
        solicitacao.setStatus(StatusSolicitacao.valueOf(dto.status())); // Converte a String de volta para enum
        return solicitacao;
    }

}
