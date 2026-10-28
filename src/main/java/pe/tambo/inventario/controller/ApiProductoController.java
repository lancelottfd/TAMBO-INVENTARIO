package pe.tambo.inventario.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pe.tambo.inventario.dao.ProductoRepository;
import pe.tambo.inventario.model.Producto;
import java.util.List;

/** API REST complementaria para consulta de productos. */
@RestController
@RequestMapping("/api/productos")
public class ApiProductoController {
    private final ProductoRepository repository;
    public ApiProductoController(ProductoRepository repository) { this.repository = repository; }

    @GetMapping public List<Producto> listar() { return repository.findAll(); }
    @GetMapping("/{id}") public Producto obtener(@PathVariable Long id) {
        return repository.findById(id).orElseThrow(() -> new java.util.NoSuchElementException("Producto no encontrado"));
    }
    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public Producto crear(@Valid @RequestBody Producto producto) { return repository.save(producto); }
}
