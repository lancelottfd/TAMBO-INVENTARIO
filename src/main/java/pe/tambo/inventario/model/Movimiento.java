package pe.tambo.inventario.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimientos")
public class Movimiento {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false) @JoinColumn(name = "producto_id")
    private Producto producto;
    @Column(nullable = false, length = 12) private String tipo;
    @Column(nullable = false) private int cantidad;
    @Column(length = 200) private String motivo;
    @Column(nullable = false, length = 100) private String usuario;
    @Column(nullable = false) private LocalDateTime fecha = LocalDateTime.now();

    public Movimiento() {}
    public Movimiento(Producto producto, String tipo, int cantidad, String motivo, String usuario) {
        this.producto = producto; this.tipo = tipo; this.cantidad = cantidad;
        this.motivo = motivo; this.usuario = usuario; this.fecha = LocalDateTime.now();
    }
    public Long getId() { return id; }
    public Producto getProducto() { return producto; }
    public String getTipo() { return tipo; }
    public int getCantidad() { return cantidad; }
    public String getMotivo() { return motivo; }
    public String getUsuario() { return usuario; }
    public LocalDateTime getFecha() { return fecha; }
}
