package com.api.casadoconstrutor.sght.dto;

import com.api.casadoconstrutor.sght.model.Comprovante;
import com.api.casadoconstrutor.sght.model.Solicitacao;
import com.api.casadoconstrutor.sght.user.User;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record SolicitacaoDto(
        Long id,
        @NotNull LocalDateTime data,
        @NotNull String motivo,
        int horasSolicitadas,
        @NotNull String status,
        @NotNull String userLogin,
        @NotNull Comprovante comprovante
) {
    // Converte uma entidade Solicitacao para SolicitacaoDTO
    public static SolicitacaoDto fromEntity(Solicitacao solicitacao) {
        return new SolicitacaoDto(
                solicitacao.getId(),
                solicitacao.getData(),
                solicitacao.getMotivo(),
                solicitacao.getHorasSolicitadas(),
                solicitacao.getStatus().name(),
                solicitacao.getUser().getLogin(),
                solicitacao.getComprovante()
        );
    }

    // Converte um DTO para uma entidade Solicitacao
    public static Solicitacao toEntity(SolicitacaoDto dto, User user) {
        Solicitacao solicitacao = new Solicitacao();
        solicitacao.setId(dto.id());
        solicitacao.setData(dto.data());
        solicitacao.setMotivo(dto.motivo());
        solicitacao.setHorasSolicitadas(dto.horasSolicitadas());
        //solicitacao.setStatus(StatusSolicitacao.valueOf(dto.status())); // Converte a String de volta para enum
        solicitacao.setUser(user);
        solicitacao.setComprovante(dto.comprovante());
        return solicitacao;
    }

}
