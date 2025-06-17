package upeu.ms.app.controller;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import upeu.ms.app.entity.Producto;
import upeu.ms.app.service.IProductoService;

@Tag(name = "Productos", description = "Servicios que consume la aplicacion")
@Slf4j
@RestController
@RequestMapping("/api/producto")
public class ProductoController {

	@Autowired
	private IProductoService service;
	
	@Autowired
	private Environment env;
	@Value("${server.port}")
	private Integer port;
	
	@GetMapping
	public List<Producto> listarActivos(){
		return service.findActivos();
	}
	
	@GetMapping("/all")
	public List<Producto> listar() {
		return service.findAll().stream().map(producto -> {
			//producto.setPort(Integer.parseInt(env.getProperty("local.server.port")));
			producto.setPort(port);
			return producto; 
		}).collect(Collectors.toList());
	}
	
	@GetMapping("/listar")
	public List<Producto> listar2() {
		return service.findAll().stream().map(producto -> {
			producto.setPort(Integer.parseInt(env.getProperty("local.server.port")));
			//producto.setPort(port);
			return producto; 
		}).collect(Collectors.toList());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Object> buscarById(@PathVariable(required = true) Long id){
		try {
			var producto = service.findById(id);
			if(producto.isPresent())
				return ResponseEntity.status(HttpStatus.OK).body(producto.get());
			return ResponseEntity.status(HttpStatus.OK).build();
		} catch (Exception e) {
			 return ResponseEntity.internalServerError().body(
	                    MessageFormat.format("Exception while fetching book with id: {0}", id));
		}
	}
	
	@PostMapping
	public ResponseEntity<Object> registrar(@RequestBody Producto ob){
		try {
			var created = service.registrar(ob);
			return ResponseEntity.status(HttpStatus.CREATED).body(created);
		} catch (DataAccessException e) {
			log.error("Error al registrar el producto", ob.toString(), e.getStackTrace());
			return ResponseEntity.internalServerError().body("Exception while saving Products");
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Producto> actualizar(@PathVariable Long id, @RequestBody Producto ob){
		var producto = service.findById(id);
		if(producto.isPresent()) {
			producto.get().setNombre(ob.getNombre());
			producto.get().setPrecio(ob.getPrecio());
			producto.get().setEstado(ob.getEstado());
			return ResponseEntity.status(HttpStatus.CREATED).body(service.actualizar(producto.get()));	
		}
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ob);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Producto> eliminar(@PathVariable Long id){
		var producto = service.findById(id);
		if(producto.isPresent()) {
			var estado = !producto.get().getEstado();
			producto.get().setEstado(estado);
			service.eliminar(producto.get());
			return ResponseEntity.ok().build();	
		}
		
		return ResponseEntity.badRequest().build();
	}
	
	@GetMapping("/ver/{id}")
	public Producto detalle(@PathVariable Long id) throws InterruptedException {
		
		if(id.equals(10L)) {
			throw new IllegalStateException("Producto no encontrado!");
		}
		if(id.equals(7L)) {
			TimeUnit.SECONDS.sleep(5L);
		}
		Optional<Producto> producto = service.findById(id);
		producto.get().setPort(Integer.parseInt(env.getProperty("local.server.port")));
		
		return producto.get();
	}
}
