package com.api.casadoconstrutor.sght.service;

import com.api.casadoconstrutor.sght.dto.SolicitacaoDto;
import com.api.casadoconstrutor.sght.enuns.StatusSolicitacao;
import com.api.casadoconstrutor.sght.model.Solicitacao;
import com.api.casadoconstrutor.sght.repository.SolicitacaoRepository;
import com.api.casadoconstrutor.sght.user.User;
import com.api.casadoconstrutor.sght.user.UserRole;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SolicitacaoService {

    @Autowired
    SolicitacaoRepository solicitacaoRepository;

    public Solicitacao save(Solicitacao solicitacao) {
        return solicitacaoRepository.save(solicitacao);
    }

    public List<Solicitacao> getSolicitacoesByUser(User user) {
        return solicitacaoRepository.findByUserId(user.getId());
    }

    public Solicitacao criarSolicitacao(Solicitacao solicitacao, User user) {
        solicitacao.setUser(user);
        solicitacao.setStatus(StatusSolicitacao.PENDENTE); // Default para novas solicitações
        return solicitacaoRepository.save(solicitacao);
    }

    public Solicitacao aprovarOuRejeitarSolicitacao(Long solicitacaoId, StatusSolicitacao novoStatus, User user) throws AccessDeniedException {
        if (user.getRole() != UserRole.ADMIN) {
            throw new AccessDeniedException("Apenas usuários ADMIN podem alterar o status de solicitações.");
        }

        Solicitacao solicitacao = solicitacaoRepository.findById(solicitacaoId)
                .orElseThrow(() -> new EntityNotFoundException("Solicitação não encontrada."));
        solicitacao.setStatus(novoStatus);
        return solicitacaoRepository.save(solicitacao);
    }

    public List<Solicitacao> findAll() {
        return solicitacaoRepository.findAll();
    }

    public List<SolicitacaoDto> findAllSolicitacoesWithUsers() {
        List<Solicitacao> solicitacoes = solicitacaoRepository.findAllWithUser();
        return solicitacoes.stream()
                .map(SolicitacaoDto::fromEntity)
                .toList();
    }

    public Optional<Solicitacao> findById(Long id) {
        return solicitacaoRepository.findById(id);
    }

    public void delete(Solicitacao solicitacao) {
        solicitacaoRepository.delete(solicitacao);
    }

    public void marcarTodasComoVistas() {
        List<Solicitacao> naoVistas = solicitacaoRepository.findNaoVistas();
        naoVistas.forEach(s -> s.setVisto(true));
        solicitacaoRepository.saveAll(naoVistas);
    }

    public List<SolicitacaoDto> buscarNaoVistas() {
        List<Solicitacao> naoVistas = solicitacaoRepository.findByVistoFalse();
        return naoVistas.stream()
                .map(SolicitacaoDto::fromEntity)
                .collect(Collectors.toList());
    }


}
