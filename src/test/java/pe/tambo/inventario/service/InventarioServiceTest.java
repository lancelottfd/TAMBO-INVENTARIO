package pe.tambo.inventario.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pe.tambo.inventario.dao.*;
import pe.tambo.inventario.model.Producto;
import pe.tambo.inventario.model.Categoria;
import java.math.BigDecimal;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InventarioServiceTest {
    private ProductoRepository productos;
    private CategoriaRepository categorias;
    private ProveedorRepository proveedores;
    private MovimientoRepository movimientos;
    private InventarioService service;

    @BeforeEach void setUp() {
        productos = mock(ProductoRepository.class);
        categorias = mock(CategoriaRepository.class);
        proveedores = mock(ProveedorRepository.class);
        movimientos = mock(MovimientoRepository.class);
        service = new InventarioService(productos, categorias, proveedores, movimientos);
    }

    @Test void salidaDescuentaStockYGuardaMovimiento() {
        Producto p = new Producto();
        p.setCodigo("BEB-01"); p.setNombre("Agua"); p.setPrecio(new BigDecimal("2.50")); p.setStock(10);
        when(productos.findById(1L)).thenReturn(Optional.of(p));
        when(productos.save(any(Producto.class))).thenAnswer(inv -> inv.getArgument(0));
        when(movimientos.save(any())).thenAnswer(inv -> inv.getArgument(0));

        service.registrarMovimiento(1L, "SALIDA", 3, "Venta interna", "admin");

        assertEquals(7, p.getStock());
        verify(movimientos).save(any());
    }

    @Test void salidaNoPermiteStockNegativo() {
        Producto p = new Producto(); p.setStock(2);
        when(productos.findById(1L)).thenReturn(Optional.of(p));

        assertThrows(IllegalArgumentException.class,
                () -> service.registrarMovimiento(1L, "SALIDA", 3, "Prueba", "admin"));
        verify(productos, never()).save(any());
    }

    @Test void cantidadDebeSerPositiva() {
        assertThrows(IllegalArgumentException.class,
                () -> service.registrarMovimiento(1L, "ENTRADA", 0, "Prueba", "admin"));
    }
}
