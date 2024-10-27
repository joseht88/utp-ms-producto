package upeu.ms.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import upeu.ms.app.entity.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

	List<Producto> findByEstadoTrue();
}
