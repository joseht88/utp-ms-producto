package upeu.ms.app.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import upeu.ms.app.entity.Producto;
import upeu.ms.app.repository.ProductoRepository;
import upeu.ms.app.service.impl.ProductoServiceImpl;

/**
 * @author Jose Bustamante
 * @Date 3 may. 2024 - 11:10:14 a. m.
 */
/* @ExtendWith(MockitoExtension.class)
 * La clase de prueba se anota con @ExtendWith(MockitoExtension.class), 
 * habilitando anotaciones de Mockito dentro del entorno de prueba. 
 * Esto facilita la creación de simulacion y la inyección de simulacion 
 * en la clase que se está probando sin configuración manual.
 */
@ExtendWith(MockitoExtension.class)
class IProductoServiceTest {
	/* @Mock private ProductoRepository repoMock
	 * Crea una versión simulada de ProductoRepository, que es una dependencia de BookService.
	 */
	@Mock
	private ProductoRepository repoMock;
	/* @InjectMocks private ProductoServiceImpl serviceMock;
	 * Crea una instancia de ProductoServiceImpl e inyecta a los simulados ProductoRepository.
	 */
	@InjectMocks
	private ProductoServiceImpl serviceMock;
	private Producto producto;
	private Producto saveProducto;
	private List<Producto> productoList;
	/* Configuración de la prueba (método @BeforeEach): 
	 * Inicializa objetos comunes utilizados en múltiples pruebas, 
	 * como una muestra objeto Producto y una lista de libros, 
	 * para evitar la repetición en cada método de prueba.
	 */
	@BeforeEach
	void setup() {
		producto = Producto.builder()
				.nombre("Zapatillas")
				.precio(2.0)
				.createAt(LocalDateTime.now())
				.estado(true)
				.build();

		saveProducto = Producto.builder()
				.id((long) 1)
				.nombre("Zapatillas")
				.precio(2.0).createAt(LocalDateTime.now())
				.estado(true)
				.build();

		productoList = List.of(producto, saveProducto);
	}
	
	@DisplayName("Service - Registrar y retornar producto")
	@Test
	void service_Saved_Return_Producto() {
		//1. preparacion
		when(this.repoMock.save(any())).thenReturn(saveProducto);
		Producto saveProducto = null;
		//2. ejecucion
		try {
			saveProducto = this.serviceMock.registrar(producto);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		//3. comparacion
		assertThat(saveProducto).isNotNull();
		assertThat(saveProducto.getId()).isNotNull();
	}
	
	@DisplayName("Service - Registrar y retornar Exception")
	@Test
	void service_Save_HandleException() {
		when(this.repoMock.save(any())).thenThrow(RuntimeException.class);
		Producto saveProducto = null;
		try {
			saveProducto = this.serviceMock.registrar(producto);
			assertThat(saveProducto.getId()).isNotNull();
		} catch (Exception e) {
			assertThat(e).isInstanceOf(RuntimeException.class);
		}
	}
	
	@DisplayName("Service - Buscar y retornar todos los productos")
	@Test
	void service_FindAll_ReturnsAllRecords() {
		when(this.repoMock.findAll()).thenReturn(productoList);
		List<Producto> list = this.serviceMock.findAll();
		assertThat(list.size()).isGreaterThan(1);
	}
	
	@DisplayName("Service - Buscar y retornar por Id un producto")
	@Test
	void service_FindById_ReturnObjectId() {
		when(this.repoMock.findById(anyLong())).thenReturn(Optional.of(saveProducto));
		Optional<Producto> productoWithId = null;
		try {
			productoWithId = this.serviceMock.findById((long) 1);
			assertThat(productoWithId.get().getId()).isNotNull();
		} catch (Exception e) {
			assertThat(e).isInstanceOf(RuntimeException.class);
		}
	}
	
	@DisplayName("Service - Buscar por Id y retornar un objeto vacio")
	@Test
	void service_FindById_ReturnsNoRecord() {
		when(this.repoMock.findById(anyLong())).thenReturn(Optional.empty());
		Optional<Producto> productoWithId = null;
		try {
			productoWithId = this.serviceMock.findById((long) 1);
			assertThat(productoWithId.get().getId()).isNull();
		} catch (Exception e) {
			assertThat(e).isInstanceOf(RuntimeException.class);
		}
	}
	
	@DisplayName("Service - Buscar y retornar por Id una Excepcion")
	@Test
	void service_FindById_HandleException() {
		when(this.repoMock.findById(anyLong())).thenThrow(RuntimeException.class);
		Optional<Producto> productoWithId = null;
		try {
			productoWithId = this.serviceMock.findById((long) 1);
			assertThat(productoWithId.get().getId()).isNull();
		} catch (Exception e) {
			assertThat(e).isInstanceOf(RuntimeException.class);
		}
	}

}
