package upeu.ms.app.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import upeu.ms.app.entity.Producto;

/**
 * @author Jose Bustamante
 * @Date 3 may. 2024 - 10:58:08 a. m.
 */
@DataJpaTest()
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class ProductoRepositoryTest {

	@Autowired
	private ProductoRepository repo;

	@DisplayName("UT01 - Registrar un producto")
	@Test
	void repo_Save_ReturnSavebProducto() {
		Producto producto = Producto.builder()
				.nombre("Adidas")
				.precio(2.0)
				.createAt(LocalDateTime.now())
				.estado(true)
				.build();

		Producto saveProducto = repo.save(producto);
		assertNotNull(saveProducto);
		assertEquals(saveProducto.getNombre(), "Adidas");
	}
	
	@DisplayName("UT02 - Retornar mas de un objeto")
	@Test
	void repo_Fetch_ReturnMoreThanOneProducto() {
		Producto producto1 = Producto.builder()
				.nombre("Adidas")
				.precio(2.0)
				.createAt(LocalDateTime.now())
				.estado(true)
				.build();

		Producto producto2 = Producto.builder()
				.nombre("Skechers")
				.precio(2.5)
				.createAt(LocalDateTime.now())
				.estado(true)
				.build();

		List<Producto> productos = List.of(producto1, producto2);
		repo.saveAll(productos);

		List<Producto> fetchedProductoList = repo.findAll();

		assertNotNull(fetchedProductoList);
		assertTrue(fetchedProductoList.size() > 1);
	}
	
	@DisplayName("UT03 - Buscar por Id, retorna el objeto")
	@Test
	void repo_FindById_ReturnSpecificProducto() {
		Producto producto = Producto.builder()
				.nombre("Adidas")
				.precio(2.0)
				.createAt(LocalDateTime.now())
				.estado(true)
				.build();

		Producto saveProducto = repo.save(producto);
		Producto specificProducto = repo.findById(saveProducto.getId()).get();
		assertNotNull(specificProducto);
		assertEquals(specificProducto.getId(), saveProducto.getId());
	}
	
	@DisplayName("UT04 - Eliminar producto")
	@Test
	void repo_Delete_ReturnProductoIsEmpty() {
		Producto producto = Producto.builder()
				.nombre("Adidas")
				.precio(2.0)
				.createAt(LocalDateTime.now())
				.estado(true)
				.build();
		Producto saveProducto = repo.save(producto);
		repo.deleteById(saveProducto.getId());
		Optional<Producto> returnProducto = repo.findById(saveProducto.getId());
		
		assertTrue(returnProducto.isEmpty());
	}
	
	@DisplayName("UT05 - Listar objetos activos")
	@Test
	void repo_findAll_ReturnProductoActive() {
		Producto producto1 = Producto.builder()
				.nombre("Adidas")
				.precio(2.0)
				.createAt(LocalDateTime.now())
				.estado(true)
				.build();

		Producto producto2 = Producto.builder()
				.nombre("Skechers")
				.precio(2.5)
				.createAt(LocalDateTime.now())
				.estado(false)
				.build();

		List<Producto> productos = List.of(producto1, producto2);
		repo.saveAll(productos);

		List<Producto> fetchedProductoList = repo.findByEstadoTrue();
		
		assertTrue(fetchedProductoList.size()==1);
		assertEquals(fetchedProductoList.size(), 1);
		assertEquals(fetchedProductoList.get(0).getNombre(), "Adidas");
	}
}
