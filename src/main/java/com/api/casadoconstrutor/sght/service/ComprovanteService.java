package com.api.casadoconstrutor.sght.service;

import com.api.casadoconstrutor.sght.model.Comprovante;
import com.api.casadoconstrutor.sght.repository.ComprovanteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class ComprovanteService {

    private final String uploadDir = "uploads/";

    @Autowired
    ComprovanteRepository comprovanteRepository;

    public Comprovante uploadFile(MultipartFile file) throws IOException {
        // Cria o diretório de upload se não existir
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Salva o arquivo no diretório
        String fileName = file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);

        // Arquiva no banco de dados
        Comprovante comprovante = new Comprovante();
        comprovante.setNomeArquivo(fileName);
        comprovante.setTipoArquivo(file.getContentType());
        comprovante.setTamanhoArquivo(file.getSize());

        return comprovanteRepository.save(comprovante);
    }

    public byte[] downloadFile(Long id) throws IOException {
        Comprovante comprovante = comprovanteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Documento não encontrado!!"));

        Path filePath = Paths.get(uploadDir, comprovante.getNomeArquivo());
        return Files.readAllBytes(filePath);
    }

    public List<Comprovante> getAllComprovantes() {
        return comprovanteRepository.findAll();
    }

    public List<Comprovante> findByNomeArquivo(String nomeArquivo) {
        return comprovanteRepository.findByNomeArquivoContaining(nomeArquivo);
    }

    public Optional<Comprovante> findById(Long id) {
        return comprovanteRepository.findById(id);
    }

}
