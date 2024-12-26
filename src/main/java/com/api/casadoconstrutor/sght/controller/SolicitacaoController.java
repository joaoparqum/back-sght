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
import java.util.Optional;

@RestController
@RequestMapping("/solicitacoes")
public class SolicitacaoController {

    final SolicitacaoService solicitacaoService;

    public SolicitacaoController(SolicitacaoService solicitacaoService) {
        this.solicitacaoService = solicitacaoService;
    }

    @GetMapping("/user")
    public List<SolicitacaoDto> listarSolicitacoes(@AuthenticationPrincipal User user) {
        return solicitacaoService.getSolicitacoesByUser(user)
                .stream()
                .map(SolicitacaoDto::fromEntity)
                .toList();
    }

    @GetMapping
    public ResponseEntity<List<SolicitacaoDto>> getAllSolicitacoes() {
        List<SolicitacaoDto> solicitacoes = solicitacaoService.findAllSolicitacoesWithUsers();
        return ResponseEntity.ok(solicitacoes);
    }

    @PostMapping
    public ResponseEntity<SolicitacaoDto> criarSolicitacao(@AuthenticationPrincipal User user, @RequestBody SolicitacaoDto dto) {
        Solicitacao solicitacao = solicitacaoService.criarSolicitacao(SolicitacaoDto.toEntity(dto, user), user);
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTarefa(@PathVariable(value = "id") Long id){
        Optional<Solicitacao> solOptional = solicitacaoService.findById(id);
        if (!solOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Solicitação não encontrada!");
        }
        solicitacaoService.delete(solOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Solicitação deletada com sucesso!");
    }

}
