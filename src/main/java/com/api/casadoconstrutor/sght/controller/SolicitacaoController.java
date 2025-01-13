package com.api.casadoconstrutor.sght.controller;

import com.api.casadoconstrutor.sght.dto.HorasDto;
import com.api.casadoconstrutor.sght.dto.SimpleSolicitacaoDto;
import com.api.casadoconstrutor.sght.dto.SolicitacaoDto;
import com.api.casadoconstrutor.sght.enuns.Filial;
import com.api.casadoconstrutor.sght.enuns.StatusSolicitacao;
import com.api.casadoconstrutor.sght.model.HorasValidas;
import com.api.casadoconstrutor.sght.model.Solicitacao;
import com.api.casadoconstrutor.sght.service.SolicitacaoService;
import com.api.casadoconstrutor.sght.user.User;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
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

    @GetMapping("/nome/{login}")
    public ResponseEntity<Object> getSolicitacaoByNome(@PathVariable(value = "login") String login) {
        List<Solicitacao> solicitacoes = solicitacaoService.findByUserLogin(login);
        if (solicitacoes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Solicitação não encontrada!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(solicitacoes);
    }

    @GetMapping("/motivo/{motivo}")
    public ResponseEntity<Object> getSolicitacaoByMotivo(@PathVariable(value = "motivo") String motivo) {
        List<Solicitacao> solicitacao = solicitacaoService.findByMotivo(motivo);
        if(solicitacao.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Solicitação não encontrada!!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(solicitacao);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneSolicitacao(@PathVariable(value = "id") Long id) {
        Optional<Solicitacao> solicitacao = solicitacaoService.findById(id);
        if(!solicitacao.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Solicitação não encontrada!!");
        }
        return ResponseEntity.ok(SimpleSolicitacaoDto.fromEntity(solicitacao.get()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateSolicitacao(@PathVariable(value = "id") Long id, @RequestBody
                                                    @Valid SimpleSolicitacaoDto solicitacaoDto) {
        Optional<Solicitacao> solicitacaoOptional = solicitacaoService.findById(id);
        if(!solicitacaoOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Solicitação não encontrada!!");
        }
        Solicitacao solicitacao = solicitacaoOptional.get();

        solicitacao.setData(solicitacaoDto.data());
        solicitacao.setMotivo(solicitacaoDto.motivo());
        solicitacao.setHorasSolicitadas(solicitacaoDto.horasSolicitadas());

        solicitacaoService.save(solicitacao);

        return ResponseEntity.status(HttpStatus.OK).body(SimpleSolicitacaoDto.fromEntity(solicitacao));
    }

    @PostMapping
    public ResponseEntity<SolicitacaoDto> criarSolicitacao(@AuthenticationPrincipal User user, @RequestBody SolicitacaoDto dto) {
        Solicitacao solicitacao = solicitacaoService.criarSolicitacao(SolicitacaoDto.toEntity(dto, user), user);
        return ResponseEntity.status(HttpStatus.CREATED).body(SolicitacaoDto.fromEntity(solicitacao));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<SolicitacaoDto> alterarStatus(
            @AuthenticationPrincipal User user,
            @PathVariable("id") Long id,
            @RequestParam("status") StatusSolicitacao status) throws AccessDeniedException {
        Solicitacao solicitacao = solicitacaoService.aprovarOuRejeitarSolicitacao(id, status, user);
        return ResponseEntity.ok(SolicitacaoDto.fromEntity(solicitacao));
    }

    @PutMapping("/marcar-vistas")
    public ResponseEntity<Void> marcarVistas() {
        solicitacaoService.marcarTodasComoVistas();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/nao-vistas")
    public ResponseEntity<List<SolicitacaoDto>> getNaoVistas() {
        List<SolicitacaoDto> naoVistas = solicitacaoService.buscarNaoVistas();
        return ResponseEntity.ok(naoVistas);
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

    @PatchMapping("/{id}")
    public ResponseEntity<SolicitacaoDto> alterarSolicitacao(@PathVariable("id") Long id,
                                                       @RequestBody Map<String, String> params) {

        Optional<Solicitacao> solicitacaoOptional = solicitacaoService.findById(id);
        if (solicitacaoOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Solicitacao solicitacao = solicitacaoOptional.get();

        params.forEach((key, value) -> {
            switch (key) {
                case "data":
                    solicitacao.setData(LocalDateTime.parse(value));
                    break;
                case "motivo":
                    solicitacao.setMotivo(value);
                    break;
                case "horasSolicitadas":
                    solicitacao.setHorasSolicitadas(Integer.parseInt(value));
                    break;
                default:
                    break;
            }
        });

        Solicitacao solicitacoesAtualizadas = solicitacaoService.save(solicitacao);
        return ResponseEntity.status(HttpStatus.OK).body(SolicitacaoDto.fromEntity(solicitacoesAtualizadas));
    }

}
