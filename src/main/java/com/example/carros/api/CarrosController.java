package com.example.carros.api;

import com.example.carros.domain.Carro;
import com.example.carros.domain.CarroService;
import com.example.carros.domain.dto.CarroDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/carros")
public class CarrosController {

    @Autowired
    private CarroService carroService;

    @GetMapping()
    public ResponseEntity<List<CarroDTO>> get(){
        return ResponseEntity.ok((carroService.getCarros()));
        //return new ResponseEntity<>(carroService.getCarros(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity getCarroById(@PathVariable("id") Long id){
        Optional<CarroDTO> carro = carroService.getCarroById(id);
        return carro
                .map(c -> ResponseEntity.ok(c))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity getCarroById(@PathVariable("tipo") String tipo){
        List<CarroDTO> listaCarros = carroService.getCarroByTipo(tipo);
        if(listaCarros.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(listaCarros);
        }
    }

    @PostMapping
    public ResponseEntity post(@RequestBody Carro carro){
        try {
            CarroDTO c = carroService.insert(carro);
            URI location = getURI(c.getId());
            return ResponseEntity.created(location).build();
        } catch (Exception ex){
            return ResponseEntity.badRequest().build();
        }
    }
    private URI getURI(Long id){
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
    }

    @PutMapping("/{id}")
    public ResponseEntity put(@PathVariable("id") Long id,@RequestBody Carro carro) {

        carro.setId(id);

        CarroDTO c = carroService.update(carro, id);

        return c != null ?
                ResponseEntity.ok(c) :
                ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id){
        boolean ok = carroService.delete(id);
        return ok ?
                ResponseEntity.ok().build() :
                ResponseEntity.notFound().build();
    }

}
