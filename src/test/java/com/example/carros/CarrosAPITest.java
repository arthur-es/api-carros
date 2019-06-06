package com.example.carros;

import com.example.carros.domain.Carro;
import com.example.carros.domain.CarroService;
import com.example.carros.domain.dto.CarroDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import javax.xml.ws.Response;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CarrosApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CarrosAPITest {

	@Autowired
	protected TestRestTemplate rest;

	@Autowired
	private CarroService service;

	private ResponseEntity<CarroDTO> getCarro(String url) {
		return
				rest.withBasicAuth("user","123").getForEntity(url, CarroDTO.class);
	}

	private ResponseEntity<List<CarroDTO>> getCarros(String url) {
		return rest.withBasicAuth("user","123").exchange(
				url,
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<List<CarroDTO>>() {
				});
	}

	@Test
	public void testSave(){
		Carro carro = new Carro();
		carro.setNome("Uno Sporting 1.4");
		carro.setTipo("passeio");

		//insert
		ResponseEntity response = rest.postForEntity("/api/v1/carros", carro, null);
		System.out.println(response);

		//verifica se criou
		assertEquals(response.getStatusCode(), HttpStatus.CREATED);

		//buscar objeto
		String location = response.getHeaders().get("location").get(0);
		CarroDTO c = getCarro(location).getBody();

		assertNotNull(c);
		assertEquals("Uno Sporting 1.4", c.getNome());
		assertEquals("passeio", c.getTipo());

		//DELETANDO
		rest.delete(location);

		//verificar se deletou
		assertEquals(HttpStatus.NOT_FOUND, getCarro(location).getStatusCode());
	}

	@Test
	public void testListaCarros(){
		List<CarroDTO> carros = (List<CarroDTO>) getCarros("/api/v1/carros").getBody();
		assertNotNull(carros);
		assertEquals(31, carros.size());
	}

	@Test
	public void testGetCarroById(){
		CarroDTO carro = getCarro("/api/v1/carros/3").getBody();

		assertNotNull(carro);
		assertEquals("Chevrolet Impala Coupe", carro.getNome());
		assertEquals("classicos", carro.getTipo());
	}

	@Test
	public void testGetCarrosByTipo(){
		List<CarroDTO> carrosLuxuosos = getCarros("/api/v1/carros/tipo/luxo").getBody();
		List<CarroDTO> carrosClassicos = getCarros("/api/v1/carros/tipo/classicos").getBody();
		List<CarroDTO> carrosEsportivos = getCarros("/api/v1/carros/tipo/esportivos").getBody();

		assertNotNull(carrosLuxuosos);
		assertNotNull(carrosClassicos);
		assertNotNull(carrosEsportivos);

		assertEquals(11, carrosLuxuosos.size());
		assertEquals(10, carrosClassicos.size());
		assertEquals(10, carrosEsportivos.size());

		//tipo nao existente
		assertEquals(HttpStatus.NO_CONTENT, getCarros("/api/v1/carros/tipo/baratos").getStatusCode());
	}

	@Test
	public void testGetOk(){
		ResponseEntity<CarroDTO> response = getCarro("/api/v1/carros/10");
		assertEquals(response.getStatusCode(), HttpStatus.OK);

		CarroDTO carro10 = response.getBody();
		assertEquals("Ford Mustang 1976", carro10.getNome());
	}

	@Test
	public void testGetNotFound(){
		ResponseEntity<CarroDTO> response = getCarro("/api/v1/carros/51");
		assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
	}




}
