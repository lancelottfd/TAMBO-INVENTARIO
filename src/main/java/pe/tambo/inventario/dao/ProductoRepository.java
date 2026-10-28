package pe.tambo.inventario.dao;
import pe.tambo.inventario.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    Optional<Producto> findByCodigoIgnoreCase(String codigo);
    List<Producto> findByNombreContainingIgnoreCaseOrCodigoContainingIgnoreCase(String nombre, String codigo);
    List<Producto> findByStockLessThanEqual(int stock);
    long countByStock(int stock);
}
