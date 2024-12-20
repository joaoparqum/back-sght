package com.api.casadoconstrutor.sght.controller;

import com.api.casadoconstrutor.sght.dto.SolicitacaoDto;
import com.api.casadoconstrutor.sght.enuns.StatusSolicitacao;
import com.api.casadoconstrutor.sght.model.Solicitacao;
import com.api.casadoconstrutor.sght.service.SolicitacaoService;
import com.api.casadoconstrutor.sght.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/solicitacoes")
public class SolicitacaoController {

    final SolicitacaoService solicitacaoService;

    public SolicitacaoController(SolicitacaoService solicitacaoService) {
        this.solicitacaoService = solicitacaoService;
    }

    @GetMapping
    public List<SolicitacaoDto> listarSolicitacoes(@AuthenticationPrincipal User user) {
        return solicitacaoService.getSolicitacoes(user)
                .stream()
                .map(SolicitacaoDto::fromEntity)
                .toList();
    }

    @PostMapping
    public ResponseEntity<SolicitacaoDto> criarSolicitacao(@AuthenticationPrincipal User user, @RequestBody SolicitacaoDto dto) {
        Solicitacao solicitacao = solicitacaoService.criarSolicitacao(SolicitacaoDto.toEntity(dto), user);
        return ResponseEntity.status(HttpStatus.CREATED).body(SolicitacaoDto.fromEntity(solicitacao));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<SolicitacaoDto> alterarStatus(
            @AuthenticationPrincipal User user,
            @PathVariable Long id,
            @RequestParam StatusSolicitacao status) throws AccessDeniedException {
        Solicitacao solicitacao = solicitacaoService.aprovarOuRejeitarSolicitacao(id, status, user);
        return ResponseEntity.ok(SolicitacaoDto.fromEntity(solicitacao));
    }
}
