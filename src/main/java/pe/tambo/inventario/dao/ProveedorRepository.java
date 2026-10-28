package pe.tambo.inventario.dao;
import pe.tambo.inventario.model.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {}
