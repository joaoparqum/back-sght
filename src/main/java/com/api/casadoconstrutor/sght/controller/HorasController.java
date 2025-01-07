package com.api.casadoconstrutor.sght.controller;

import com.api.casadoconstrutor.sght.dto.HorasDto;
import com.api.casadoconstrutor.sght.model.HorasValidas;
import com.api.casadoconstrutor.sght.service.HorasService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/horas")
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
    public ResponseEntity<Object> getOneHoras(@PathVariable(value = "id") Long id) {
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





}
