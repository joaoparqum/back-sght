package com.api.casadoconstrutor.sght.dto;

import com.api.casadoconstrutor.sght.model.Solicitacao;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record SimpleSolicitacaoDto(
        Long id,
        @NotNull LocalDateTime data,
        @NotNull String motivo,
        int horasSolicitadas
) {
    public static SimpleSolicitacaoDto fromEntity(Solicitacao solicitacao) {
        return new SimpleSolicitacaoDto(
                solicitacao.getId(),
                solicitacao.getData(),
                solicitacao.getMotivo(),
                solicitacao.getHorasSolicitadas()
        );
    }
}
