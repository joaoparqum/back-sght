package com.api.casadoconstrutor.sght.dto;

import com.api.casadoconstrutor.sght.enuns.Filial;
import com.api.casadoconstrutor.sght.model.HorasValidas;
import jakarta.validation.constraints.NotNull;

import java.time.Duration;

public record HorasDto(
        Long id,
        @NotNull String nomeColaborador,
        @NotNull Filial filial,
        @NotNull String junhoJulho,
        @NotNull String agosto,
        @NotNull String setembroOutubro,
        @NotNull String novembro,
        @NotNull String dezembro,
        @NotNull String janeiro,
        @NotNull String fevereiro,
        @NotNull String marco,
        @NotNull String abril,
        @NotNull String maio,
        @NotNull String junho
) {

    public static HorasDto fromEntity(HorasValidas horasValidas) {
        return new HorasDto(
                horasValidas.getId(),
                horasValidas.getNomeColaborador(),
                horasValidas.getFilial(),
                horasValidas.getJunhoJulho(),
                horasValidas.getAgosto(),
                horasValidas.getSetembroOutubro(),
                horasValidas.getNovembro(),
                horasValidas.getDezembro(),
                horasValidas.getJaneiro(),
                horasValidas.getFevereiro(),
                horasValidas.getMarco(),
                horasValidas.getAbril(),
                horasValidas.getMaio(),
                horasValidas.getJunho()
        );
    }

}
