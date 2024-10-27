package upeu.ms.app.service;

import java.util.List;
import java.util.Optional;

import upeu.ms.app.entity.Producto;

public interface IProductoService {

	public List<Producto> findAll();
	
	public List<Producto> findActivos();
	
	public Producto registrar(Producto ob);
	
	public Producto actualizar(Producto ob);
	
	public void eliminar(Long id);
	
	public void eliminar(Producto ob);
	
	public Optional<Producto> findById(Long id);
}
