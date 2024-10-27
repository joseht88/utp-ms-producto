package upeu.ms.app.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.containsInAnyOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import upeu.ms.app.entity.Producto;
import upeu.ms.app.service.impl.ProductoServiceImpl;

/**
 * @author Jose Bustamante
 * @Date 3 may. 2024 - 11:13:16 a. m.
 */

/* @WebMvcTest(ProductoController.class)
 * Esta anotación especifica que el contexto de prueba solo debe configurar en la capa web, 
 * centrándose especialmente en ProductoController. No carga todo el contexto, 
 * haciendo que las pruebas sean más rápidas y enfocadas.
 */
@WebMvcTest(ProductoController.class)
class ProductoControllerTest {
	/* @Autowired private MockMvc mockMvc;
	 * Este campo se inyecta automáticamente con una instancia de MockMvc, 
	 * un punto de entrada principal para las pruebas Spring MVC del lado del servidor. 
	 * Permite enviar solicitudes HTTP a ProductoController y afirmando respuestas.
	 */
	@Autowired
	private MockMvc mockMvc;
	/* @MockBean private ProductoServiceImpl service;
	 * Declara un Bean simulado para BookServiceImpl para ser utilizado en el contexto de la aplicación. 
	 * Este simulador reemplazará al bean real BookServiceImpl en el contexto de prueba, 
	 * lo que permite el troceado y la verificación del comportamiento sin depender 
	 * de la lógica real de la capa de servicio.
	 */
	@MockBean
	private ProductoServiceImpl service;
	private Producto producto;
	private Producto saveProducto;
	private List<Producto> list;
	/*
	 * Inicializa los datos de prueba antes de cada caso de prueba. 
	 * Crea objetos Producto que representan un bien/producto antes y después de ser guardados, 
	 * simulando datos que normalmente serían manejados por la aplicación.
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
				.precio(2.0)
				.createAt(LocalDateTime.now())
				.estado(true)
				.build();
	}
	
	@DisplayName("Controller - Registrar y retornar un producto")
	@Test
	public void save_ReturnsSuccess() throws Exception {
		when(this.service.registrar(any())).thenReturn(saveProducto);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		String content = objectMapper.writeValueAsString(producto);
		this.mockMvc.perform(post("/api/producto")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content))
		.andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.nombre").value("Zapatillas"))
        .andExpect(jsonPath("$.precio").value("2.0"));
	}
	
	@DisplayName("Controller - Registrar y retornar Exception")
	@Test
    public void save_ReturnsInternalServerError() throws Exception {
        when(this.service.registrar(any())).thenThrow(RuntimeException.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(producto);
        this.mockMvc.perform(post("/api/producto")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content))
        .andExpect(status().isInternalServerError());
    }
	
	@DisplayName("Controller - Buscar por Id y retornar objeto")
	@Test
    public void findById_ReturnsSuccess() throws Exception {
        when(this.service.findById(anyLong())).thenReturn(Optional.of(saveProducto));
        this.mockMvc.perform(get("/api/producto/1"))
        		.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.nombre").value("Zapatillas"))
                .andExpect(jsonPath("$.precio").value("2.0"));
    }
	
	@DisplayName("Controller - Buscar por Id y retornar Exception")
    @Test
    public void findById_ReturnsInternalServerError() throws Exception {
        when(this.service.findById(anyLong())).thenThrow(RuntimeException.class);
        this.mockMvc.perform(get("/api/producto/1"))
        .andExpect(status().isInternalServerError());
    }
	
	@DisplayName("Controller - Buscar todos los elementos")
    @Test
    public void findAll_ReturnsSuccess() throws Exception {
		this.list = Arrays.asList(saveProducto);
        when(this.service.findAll()).thenReturn(this.list);
        this.mockMvc.perform(get("/api/producto/all"))
        	.andExpect(status().isOk())
        	.andExpect(content().contentType(MediaType.APPLICATION_JSON))
        	.andDo(print())
        	.andExpect(jsonPath("$", hasSize(1)))
        //.andExpect(jsonPath("$[0].nombre", Matchers.equalTo("Zapatillas")))
        //.andExpect(jsonPath("$[1].nombre", Matchers.equalTo("Dramático")));
        // Verifica los datos esperados en la respuesta
        	.andExpect(jsonPath("$[*].nombre").value(containsInAnyOrder("Zapatillas")));
    }
}
