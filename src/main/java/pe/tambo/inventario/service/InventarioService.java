package pe.tambo.inventario.service;

import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.tambo.inventario.dao.*;
import pe.tambo.inventario.model.*;
import java.util.List;

@Service
public class InventarioService {
    private static final Logger log = LoggerFactory.getLogger(InventarioService.class);
    private final ProductoRepository productos;
    private final CategoriaRepository categorias;
    private final ProveedorRepository proveedores;
    private final MovimientoRepository movimientos;

    public InventarioService(ProductoRepository productos, CategoriaRepository categorias,
            ProveedorRepository proveedores, MovimientoRepository movimientos) {
        this.productos = productos; this.categorias = categorias;
        this.proveedores = proveedores; this.movimientos = movimientos;
    }

    public List<Producto> listarProductos(String busqueda) {
        if (Strings.isNullOrEmpty(busqueda) || StringUtils.isBlank(busqueda)) return productos.findAll();
        String termino = busqueda.trim();
        return productos.findByNombreContainingIgnoreCaseOrCodigoContainingIgnoreCase(termino, termino);
    }
    public List<Categoria> listarCategorias() { return categorias.findAll(); }
    public List<Proveedor> listarProveedores() { return proveedores.findAll(); }
    public List<Movimiento> ultimosMovimientos() { return movimientos.findTop20ByOrderByFechaDesc(); }
    public List<Producto> productosConStockBajo() { return productos.findAll().stream().filter(p -> p.getStock() <= p.getStockMinimo()).toList(); }
    public long contarProductos() { return productos.count(); }
    public long contarAgotados() { return productos.countByStock(0); }
    public Producto buscarProducto(Long id) {
        return productos.findById(id).orElseThrow(() -> new IllegalArgumentException("Producto no encontrado."));
    }
    public Categoria buscarCategoria(Long id) {
        return categorias.findById(id).orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada."));
    }

    @Transactional
    public Producto guardarProducto(Producto producto, Long categoriaId) {
        producto.setCategoria(buscarCategoria(categoriaId));
        if (StringUtils.isBlank(producto.getCodigo()) || StringUtils.isBlank(producto.getNombre()))
            throw new IllegalArgumentException("Código y nombre son obligatorios.");
        return productos.save(producto);
    }
    @Transactional
    public void eliminarProducto(Long id) {
        Producto producto = buscarProducto(id);
        producto.setActivo(false);
        productos.save(producto);
    }
    @Transactional
    public Movimiento registrarMovimiento(Long productoId, String tipo, int cantidad, String motivo, String usuario) {
        if (cantidad <= 0) throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        Producto producto = buscarProducto(productoId);
        String tipoNormalizado = StringUtils.upperCase(StringUtils.trimToEmpty(tipo));
        if (!tipoNormalizado.equals("ENTRADA") && !tipoNormalizado.equals("SALIDA"))
            throw new IllegalArgumentException("El tipo debe ser ENTRADA o SALIDA.");
        if (tipoNormalizado.equals("SALIDA") && producto.getStock() < cantidad)
            throw new IllegalArgumentException("Stock insuficiente. Disponible: " + producto.getStock());
        producto.setStock(tipoNormalizado.equals("ENTRADA") ? producto.getStock() + cantidad : producto.getStock() - cantidad);
        productos.save(producto);
        Movimiento movimiento = movimientos.save(new Movimiento(producto, tipoNormalizado, cantidad,
                StringUtils.defaultIfBlank(motivo, "Sin motivo especificado"), StringUtils.defaultIfBlank(usuario, "sistema")));
        log.info("Movimiento {} registrado para producto id {} cantidad {}", tipoNormalizado, productoId, cantidad);
        return movimiento;
    }
}
