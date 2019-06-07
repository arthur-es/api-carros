package com.example.carros;

import com.example.carros.domain.Carro;
import com.example.carros.domain.CarroService;
import com.example.carros.domain.dto.CarroDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CarrosServiceTest {

	@Autowired
	private CarroService carroService;

	@Test
	public void inserirUmNovoCarro() {
		Carro carro = new Carro();
		carro.setNome("uninho");
		carro.setTipo("esportivos");

		CarroDTO c = carroService.insert(carro);
		assertNotNull(c);

		Long id = c.getId();
		assertNotNull(id);

		//Buscar o objeto
		Optional<CarroDTO> op = carroService.getCarroById(id);
		assertTrue(op.isPresent());

		c = op.get();

		assertEquals("uninho", c.getNome());
		assertEquals("esportivos", c.getTipo());

		//Deletar o objeto
		carroService.delete(id);

		//Verificar se deletou
		assertFalse(carroService.getCarroById(id).isPresent());
	}
	@Test
	public void getListaCarros(){
		List<CarroDTO> listaCarros = carroService.getCarros();
		assertEquals(62, listaCarros.size());
	}

	@Test
	public void getCarroById(){
		//cenario
		//pegar o 11° carro
		Optional<CarroDTO> op = carroService.getCarroById(11L);
		CarroDTO c = op.get();

		//acao
		String nomeOp = c.getNome();
		String tipoOp = c.getTipo();

		//verificacao
		assertTrue(op.isPresent()); //carro existe no banco
		assertEquals("Ferrari FF", nomeOp);
		assertEquals("esportivos", tipoOp);

	}

	@Test
	public void getCarroByTipo(){
		//cenario
		List<CarroDTO> listaCarrosEsportivos = carroService.getCarroByTipo("esportivos");
		List<CarroDTO> listaCarrosLuxuosos = carroService.getCarroByTipo("luxo");
		List<CarroDTO> listaCarrosClassicos = carroService.getCarroByTipo("classicos");

		//verificacao
		assertTrue(listaCarrosEsportivos.size() != 0);
		assertEquals(20 , listaCarrosEsportivos.size() );

		assertTrue(listaCarrosLuxuosos.size() != 0);
		assertEquals(22, listaCarrosLuxuosos.size());

		assertTrue(listaCarrosClassicos.size() != 0);
		assertEquals(20, listaCarrosClassicos.size());

		assertEquals(0, carroService.getCarroByTipo("barato").size()); // tipo que nao existe
	}

}
