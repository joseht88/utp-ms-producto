package upeu.ms.app.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import upeu.ms.app.entity.Producto;
import upeu.ms.app.repository.ProductoRepository;
import upeu.ms.app.service.IProductoService;

@Service
public class ProductoServiceImpl implements IProductoService {

	@Autowired
	private ProductoRepository repo;
	
	@Transactional(readOnly = true)
	@Override
	public List<Producto> findAll() {
		return repo.findAll();
	}

	@Transactional(readOnly = true)
	@Override
	public Optional<Producto> findById(Long id) {
		return repo.findById(id);
	}
	
	@Transactional(readOnly = true)
	@Override
	public List<Producto> findActivos() {
		return repo.findByEstadoTrue();
	}

	@Transactional
	@Override
	public Producto registrar(Producto ob) {
		return repo.save(ob);
	}

	@Transactional
	@Override
	public Producto actualizar(Producto ob) {
		return repo.save(ob);
	}

	@Transactional
	@Override
	public void eliminar(Long id) {
		var producto =  repo.findById(id);
		if(producto.isPresent()) {
			var estado = !producto.get().getEstado();
			producto.get().setEstado(estado);
			repo.save(producto.get());
		}
			
	}
	
	@Transactional
	@Override
	public void eliminar(Producto ob) {
		repo.save(ob);
	}

}
