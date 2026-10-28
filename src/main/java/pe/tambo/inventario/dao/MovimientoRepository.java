package pe.tambo.inventario.dao;
import pe.tambo.inventario.model.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
    List<Movimiento> findTop20ByOrderByFechaDesc();
}
