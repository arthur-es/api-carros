package com.example.carros.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CarroService {

    @Autowired
    private CarroRepository carroRepositorio;

    public Iterable<Carro> getCarros() {

        return carroRepositorio.findAll();
    }

    public Optional<Carro> getCarroById(Long id){
        return carroRepositorio.findById(id);
    }

    public Iterable<Carro> getCarroByTipo(String tipo){
        return carroRepositorio.findByTipo(tipo);
    }


    // Mock de alguns dados para testes iniciais da API
    public List<Carro> getCarrosFake(){
        List<Carro> carros = new ArrayList<>();
        carros.add(new Carro(1L, "Fusca"));
        carros.add(new Carro(1L, "Uno sporting"));
        carros.add(new Carro(1L, "Ferrari"));

        return carros;
    }

}
