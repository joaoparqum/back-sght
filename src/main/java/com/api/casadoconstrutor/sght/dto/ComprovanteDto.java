package com.api.casadoconstrutor.sght.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ComprovanteDto(
        Long id,
        @NotNull String nomeArquivo,
        @NotNull String tipoArquivo,
        Long tamanhoArquivo
) {
}
