package pe.tambo.inventario.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pe.tambo.inventario.model.Producto;
import pe.tambo.inventario.service.InventarioService;
import java.security.Principal;

@Controller
public class WebController {
    private final InventarioService service;
    public WebController(InventarioService service) { this.service = service; }

    @GetMapping("/login") public String login() { return "login"; }

    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("totalProductos", service.contarProductos());
        model.addAttribute("agotados", service.contarAgotados());
        model.addAttribute("stockBajo", service.productosConStockBajo().size());
        model.addAttribute("movimientos", service.ultimosMovimientos());
        return "index";
    }

    @GetMapping("/productos")
    public String productos(@RequestParam(defaultValue = "") String q, Model model) {
        model.addAttribute("productos", service.listarProductos(q));
        model.addAttribute("categorias", service.listarCategorias());
        model.addAttribute("q", q);
        return "productos";
    }

    @PostMapping("/productos")
    public String crearProducto(@Valid @ModelAttribute Producto producto, BindingResult result,
                                @RequestParam Long categoriaId, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("productos", service.listarProductos(""));
            model.addAttribute("categorias", service.listarCategorias());
            model.addAttribute("q", "");
            model.addAttribute("error", "Verifica los datos del producto.");
            return "productos";
        }
        try { service.guardarProducto(producto, categoriaId); }
        catch (IllegalArgumentException ex) {
            model.addAttribute("productos", service.listarProductos(""));
            model.addAttribute("categorias", service.listarCategorias());
            model.addAttribute("q", "");
            model.addAttribute("error", ex.getMessage());
            return "productos";
        }
        return "redirect:/productos?guardado";
    }

    @PostMapping("/productos/{id}/desactivar")
    public String desactivarProducto(@PathVariable Long id) {
        service.eliminarProducto(id);
        return "redirect:/productos";
    }

    @GetMapping("/movimientos")
    public String movimientos(Model model) {
        model.addAttribute("movimientos", service.ultimosMovimientos());
        model.addAttribute("productos", service.listarProductos(""));
        return "movimientos";
    }

    @PostMapping("/movimientos")
    public String registrarMovimiento(@RequestParam Long productoId, @RequestParam String tipo,
            @RequestParam int cantidad, @RequestParam(required = false) String motivo, Principal principal, Model model) {
        try {
            service.registrarMovimiento(productoId, tipo, cantidad, motivo, principal.getName());
            return "redirect:/movimientos?registrado";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("movimientos", service.ultimosMovimientos());
            model.addAttribute("productos", service.listarProductos(""));
            return "movimientos";
        }
    }

    @GetMapping("/alertas")
    public String alertas(Model model) {
        model.addAttribute("productos", service.productosConStockBajo());
        return "alertas";
    }
}
