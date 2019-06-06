package com.example.carros.domain;

import com.example.carros.domain.dto.CarroDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarroService {

    @Autowired
    private CarroRepository carroRepositorio;

    public List<CarroDTO> getCarros() {
        return carroRepositorio.findAll().stream().map(CarroDTO::create).collect(Collectors.toList());
    }

    public Optional<CarroDTO> getCarroById(Long id){
        return carroRepositorio.findById(id).map(CarroDTO::create);
    }

    public List<CarroDTO> getCarroByTipo(String tipo){
        return carroRepositorio.findByTipo(tipo).stream().map(CarroDTO::create).collect(Collectors.toList());
    }


    // Mock de alguns dados para testes iniciais da API
//    public List<Carro> getCarrosFake(){
//        List<Carro> carros = new ArrayList<>();
//        carros.add(new Carro(1L, "Fusca"));
//        carros.add(new Carro(1L, "Uno sporting"));
//        carros.add(new Carro(1L, "Ferrari"));
//
//        return carros;
//    }

    public CarroDTO insert(Carro carro) {
        Assert.isNull(carro.getId(), "Não foi possivel inserir o registro.");

        return CarroDTO.create(carroRepositorio.save(carro));
    }

    public CarroDTO update(Carro carro, Long id) {
        Assert.notNull(id,"Não foi possível atualizar o registro");

        // Busca o carro no banco de dados
        Optional<Carro> optional = carroRepositorio.findById(id);
        if(optional.isPresent()) {
            Carro db = optional.get();
            // Copiar as propriedades
            db.setNome(carro.getNome());
            db.setTipo(carro.getTipo());
            System.out.println("Carro id " + db.getId());

            // Atualiza o carro
            carroRepositorio.save(db);

            return CarroDTO.create(db);
        } else {
            return null;
            //throw new RuntimeException("Não foi possível atualizar o registro");
        }
    }

//    public Carro update(Carro carro, Long id) {
//        Assert.notNull(id, "Não foi possivel atualizar o registro!");
//
//        //busca o carro no banco de dados
//        Optional<CarroDTO> optional = getCarroById(id);
//        if(optional.isPresent()) {
//            CarroDTO db = optional.get();
//            //copiar as propriedades
//            db.setNome((carro.getNome()));
//            db.setTipo(carro.getTipo());
//            System.out.println("Carro id " + db.getId());
//            //ATUALIZA O CARRO
//            carroRepositorio.save(db);
//            return db;
//
//        } else {
//            throw new RuntimeException("Não foi possivel atualizar o registro.");
//        }
//    }

    public void delete(Long id) {
        carroRepositorio.deleteById(id);
    }
}
