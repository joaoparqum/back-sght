package com.api.casadoconstrutor.sght.service;

import com.api.casadoconstrutor.sght.model.HorasValidas;
import com.api.casadoconstrutor.sght.repository.HorasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class HorasService {

    @Autowired
    HorasRepository horasRepository;

    public String calcularTotal(HorasValidas horasValidas) {
        // Inicializando a duração total com 0
        Duration totalDuration = Duration.ZERO;

        // Lista de campos com horas
        String[] campos = {
                horasValidas.getJaneiro(), horasValidas.getFevereiro(), horasValidas.getMarco(),
                horasValidas.getAbril(), horasValidas.getMaio(), horasValidas.getJunho(),
                horasValidas.getJunhoJulho(), horasValidas.getAgosto(), horasValidas.getSetembroOutubro(),
                horasValidas.getNovembro(), horasValidas.getDezembro()
        };

        // Percorrendo os campos e somando as horas
        for (String campo : campos) {
            if (campo != null && !campo.isEmpty()) {
                totalDuration = totalDuration.plus(parseDuration(campo)); // Parse e soma as durações
            }
        }

        // Convertendo o total de horas para o formato "HH:mm:ss"
        long totalSeconds = totalDuration.getSeconds();
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;

        // Retornando o total como string no formato "HH:mm:ss"
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    private Duration parseDuration(String timeString) {
        // Detectando se é um tempo negativo
        boolean isNegative = timeString.startsWith("-");

        // Remover o sinal negativo, se houver
        if (isNegative) {
            timeString = timeString.substring(1);
        }

        // Dividindo a string de tempo no formato "HH:mm:ss"
        String[] parts = timeString.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        int seconds = Integer.parseInt(parts[2]);

        // Criando a Duration com base nas horas, minutos e segundos
        Duration duration = Duration.ofHours(hours)
                .plusMinutes(minutes)
                .plusSeconds(seconds);

        // Se for negativo, fazemos a subtração
        if (isNegative) {
            duration = duration.negated(); // Negar a duração para subtrair
        }

        return duration;
    }

    public HorasValidas save(HorasValidas horasValidas){
        return horasRepository.save(horasValidas);
    }

    public List<HorasValidas> findAll(){
        return horasRepository.findAll();
    }

    public Optional<HorasValidas> findById(Long id){
        return horasRepository.findById(id);
    }

    public List<HorasValidas> findByNomeColaborador(String nomeColaborador) {
        return horasRepository.findByNomeColaboradorContaining(nomeColaborador);
    }

    public void delete(HorasValidas horasValidas){
        horasRepository.delete(horasValidas);
    }

}
