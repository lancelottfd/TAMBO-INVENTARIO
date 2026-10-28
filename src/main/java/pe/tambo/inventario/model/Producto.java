package pe.tambo.inventario.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Entity
@Table(name = "productos")
public class Producto {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank @Column(nullable = false, unique = true, length = 40)
    private String codigo;
    @NotBlank @Column(nullable = false, length = 120)
    private String nombre;
    @Column(length = 250) private String descripcion;
    @NotNull @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;
    @NotNull @DecimalMin("0.00") @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio = BigDecimal.ZERO;
    @Min(0) @Column(nullable = false) private int stock = 0;
    @Min(0) @Column(name = "stock_minimo", nullable = false) private int stockMinimo = 0;
    private boolean activo = true;

    public Producto() {}
    public Long getId() { return id; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public int getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(int stockMinimo) { this.stockMinimo = stockMinimo; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    @Transient public String getEstadoStock() {
        if (stock == 0) return "AGOTADO";
        return stock <= stockMinimo ? "STOCK BAJO" : "DISPONIBLE";
    }
}
