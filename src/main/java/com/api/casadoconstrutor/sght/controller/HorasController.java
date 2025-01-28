package com.api.casadoconstrutor.sght.controller;

import com.api.casadoconstrutor.sght.dto.HorasDto;
import com.api.casadoconstrutor.sght.enuns.Filial;
import com.api.casadoconstrutor.sght.model.HorasValidas;
import com.api.casadoconstrutor.sght.service.HorasService;
import com.api.casadoconstrutor.sght.user.User;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/sght/horas")
public class HorasController {

    final HorasService horasService;

    public HorasController(HorasService horasService) {
        this.horasService = horasService;
    }

    @PostMapping
    public ResponseEntity<Object> saveHoras(@RequestBody @Valid HorasDto horasDto){
        HorasValidas horasValidas = new HorasValidas();
        BeanUtils.copyProperties(horasDto, horasValidas);

        String total = horasService.calcularTotal(horasValidas);
        horasValidas.setTotal(total);

        return ResponseEntity.status(HttpStatus.CREATED).body(horasService.save(horasValidas));
    }

    @GetMapping("/nome/{nomeColaborador}")
    public ResponseEntity<Object> getHorasByNome(@PathVariable(value = "nomeColaborador") String nomeColaborador) {
        List<HorasValidas> horasValidas = horasService.findByNomeColaborador(nomeColaborador);
        if(horasValidas.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Hora Válida não encontrada!!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(horasValidas);
    }

    @GetMapping
    public ResponseEntity<List<HorasValidas>> getAllHoras() {
        return ResponseEntity.status(HttpStatus.CREATED).body(horasService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneHoras(@PathVariable("id") Long id) {
        Optional<HorasValidas> horasValidas = horasService.findById(id);

        if(!horasValidas.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Hora Válida não encontrada!!");
        }

        return ResponseEntity.status(HttpStatus.OK).body(horasValidas.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteHoras(@PathVariable(value = "id") Long id) {
        Optional<HorasValidas> horasValidasOptional = horasService.findById(id);

        if(!horasValidasOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Hora Válida não encontrada!!");
        }

        horasService.delete(horasValidasOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Hora Válida deletada com sucesso!!");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateHoras(@PathVariable(value = "id") Long id,
                                                    @RequestBody @Valid HorasDto horasDto) {
        Optional<HorasValidas> horasValidasOptional = horasService.findById(id);
        if(!horasValidasOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Hora Válida não encontrada!!");
        }

        HorasValidas horasValidas = new HorasValidas();
        BeanUtils.copyProperties(horasDto, horasValidas);
        horasValidas.setId(horasValidasOptional.get().getId());
        return ResponseEntity.status(HttpStatus.OK).body(horasService.save(horasValidas));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HorasDto> alterarHoras(@PathVariable("id") Long id, @RequestBody Map<String, String> params) {

        Optional<HorasValidas> horasValidasOptional = horasService.findById(id);
        if (horasValidasOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        HorasValidas horasValidas = horasValidasOptional.get();

        params.forEach((key, value) -> {
            switch (key) {
                case "nomeColaborador":
                    horasValidas.setNomeColaborador(value);
                    break;
                case "filial":
                    horasValidas.setFilial(Filial.valueOf(value));
                    break;
                case "junhoJulho":
                    horasValidas.setJunhoJulho(value);
                    break;
                case "agosto":
                    horasValidas.setAgosto(value);
                    break;
                case "setembroOutubro":
                    horasValidas.setSetembroOutubro(value);
                    break;
                case "novembro":
                    horasValidas.setNovembro(value);
                    break;
                case "dezembro":
                    horasValidas.setDezembro(value);
                    break;
                case "janeiro":
                    horasValidas.setJaneiro(value);
                    break;
                case "fevereiro":
                    horasValidas.setFevereiro(value);
                    break;
                case "marco":
                    horasValidas.setMarco(value);
                    break;
                case "abril":
                    horasValidas.setAbril(value);
                    break;
                case "maio":
                    horasValidas.setMaio(value);
                    break;
                case "junho":
                    horasValidas.setJunho(value);
                    break;
                default:
                    break;
            }
        });

        String total = horasService.calcularTotal(horasValidas);
        horasValidas.setTotal(total);

        HorasValidas horasAtualizadas = horasService.save(horasValidas);
        return ResponseEntity.status(HttpStatus.OK).body(HorasDto.fromEntity(horasAtualizadas));
    }

}
