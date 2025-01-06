package com.api.casadoconstrutor.sght.service;

import com.api.casadoconstrutor.sght.model.HorasValidas;
import com.api.casadoconstrutor.sght.repository.HorasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HorasService {

    @Autowired
    HorasRepository horasRepository;

    public HorasValidas save(HorasValidas horasValidas){
        return horasRepository.save(horasValidas);
    }

    public List<HorasValidas> findAll(){
        return horasRepository.findAll();
    }

    public Optional<HorasValidas> findById(Long id){
        return horasRepository.findById(id);
    }

    public void delete(HorasValidas horasValidas){
        horasRepository.delete(horasValidas);
    }

}
