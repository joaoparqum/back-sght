package com.api.casadoconstrutor.sght.controller;

import com.api.casadoconstrutor.sght.model.Comprovante;
import com.api.casadoconstrutor.sght.service.ComprovanteService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/sght/comprovante")
public class ComprovanteController {

    final ComprovanteService comprovanteService;

    public ComprovanteController(ComprovanteService comprovanteService) {
        this.comprovanteService = comprovanteService;
    }

    @PostMapping("/upload")
    public ResponseEntity<Comprovante> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        Comprovante comprovante = comprovanteService.uploadFile(file);
        return new ResponseEntity<>(comprovante, HttpStatus.CREATED);
    }

    @GetMapping("/view/{id}")
    public ResponseEntity<byte[]> viewFile(@PathVariable("id") Long id) throws IOException {

        byte[] fileData = comprovanteService.downloadFile(id);
        Optional<Comprovante> comprovante = comprovanteService.findById(id);

        String fileName = comprovante.get().getNomeArquivo();
        String fileType = comprovante.get().getTipoArquivo();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(fileType));
        headers.setContentDispositionFormData("inline", fileName);

        return new ResponseEntity<>(fileData, headers, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Comprovante>> getAllComprovantes() {
        return ResponseEntity.status(HttpStatus.CREATED).body(comprovanteService.getAllComprovantes());
    }

    @GetMapping("/nomeArquivo/{nomeArquivo}")
    public ResponseEntity<Object> getComprovanteByNome(@PathVariable(value = "nomeArquivo") String nomeArquivo) {
        List<Comprovante> documento = comprovanteService.findByNomeArquivo(nomeArquivo);
        if(documento.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Documento não encontrado!!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(documento);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Object> getOneComprovante(@PathVariable(value = "id") Long id){
        Optional<Comprovante> documento = comprovanteService.findById(id);
        if (!documento.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Documento não encontrado!.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(documento.get());
    }
}
