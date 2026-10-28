# Sistema web de gestión de inventario — Tiendas Tambo (APF3)

Proyecto académico de referencia con Java 21, Spring Boot, MVC, Spring Data JPA/DAO, MySQL, Thymeleaf, Spring Security, pruebas unitarias y librerías complementarias.

## Funcionalidades incluidas
- Inicio de sesión con Spring Security y contraseña codificada mediante BCrypt.
- Panel con productos registrados, productos agotados, stock bajo y movimientos recientes.
- Registro y búsqueda de productos.
- Registro de entradas y salidas con actualización transaccional del stock.
- Validación para impedir cantidades no positivas y salidas con stock insuficiente.
- Historial reciente de movimientos y responsable autenticado.
- Alertas de productos con stock igual o inferior al mínimo.
- API REST básica `GET /api/productos` y `GET /api/productos/{id}`.
- Pruebas unitarias de la lógica de inventario.
- Logging con SLF4J/Logback.
- Uso de Guava y Apache Commons Lang en validación/búsqueda; Apache POI está incluido como dependencia para ampliar exportación de reportes.

## Requisitos
- JDK 21
- Maven 3.9+
- MySQL 8+

## 1. Crear la base de datos
Puedes ejecutar en MySQL Workbench:

```sql
CREATE DATABASE tambo_inventario CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

La aplicación usa por defecto:
- Base de datos: `tambo_inventario`
- Usuario: `root`
- Contraseña: vacía

Si tu MySQL tiene otra configuración, define variables de entorno antes de iniciar:
- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`

Hibernate crea/actualiza las tablas al arrancar (`spring.jpa.hibernate.ddl-auto=update`). Después del primer inicio, ejecuta `database/tambo_inventario.sql` para agregar categorías y un proveedor de prueba.

## 2. Ejecutar el proyecto
En la carpeta donde está `pom.xml`:

```bash
mvn clean test
mvn spring-boot:run
```

Abre `http://localhost:8080`.

## 3. Acceso de demostración
Por defecto, el usuario de demostración es:
- Usuario: `admin`
- Contraseña: `Cambiar123!`

Puedes personalizarlos con las variables `TAMBO_USER` y `TAMBO_PASSWORD`. **Cambia la contraseña antes de desplegarlo o compartirlo**. El usuario se mantiene en memoria para esta versión académica; la administración persistente de usuarios/roles queda como ampliación.

## 4. Prueba funcional recomendada
1. Inicia sesión.
2. Abre Productos y registra uno (por ejemplo, código `BEB-001`, nombre `Agua`, categoría `Bebidas`, stock inicial `10`, stock mínimo `3`).
3. Abre Movimientos y registra una ENTRADA de 5 unidades: el stock debe quedar en 15.
4. Registra una SALIDA de 4 unidades: el stock debe quedar en 11.
5. Intenta una salida mayor al stock: el sistema debe mostrar error y no modificar el stock.
6. Abre Alertas de stock para comprobar los productos que alcanzaron el mínimo.
7. Ejecuta `mvn test` para verificar las pruebas unitarias.

## Arquitectura
- `model`: entidades JPA (Producto, Categoria, Proveedor, Movimiento).
- `dao`: interfaces de acceso a datos basadas en `JpaRepository`.
- `service`: reglas de negocio y actualización transaccional de stock.
- `controller`: rutas MVC, vistas Thymeleaf y API REST.
- `config`: configuración de seguridad.

## Cómo se evidencian los principios
- **MVC:** controladores web separados de las vistas Thymeleaf y entidades/servicios.
- **DAO:** repositorios `JpaRepository` abstraen el acceso a MySQL.
- **SOLID:** `InventarioService` recibe repositorios por inyección de dependencias; los controladores delegan reglas de negocio al servicio.
- **TDD/pruebas:** pruebas unitarias de salida válida, stock insuficiente y cantidad no válida. Para demostrar TDD en la sustentación, explica el ciclo prueba-fallo-implementación-prueba exitosa y añade capturas reales de `mvn test`.
- **Seguridad:** Spring Security protege las rutas, las contraseñas se codifican con BCrypt y el usuario responsable del movimiento se obtiene de la sesión.
- **Librerías:** Guava, Apache Commons Lang, Apache POI y Logback están declaradas en Maven. POI está preparado para implementar exportación Excel, pero dicha exportación no está incluida en esta versión.

## Git y GitHub
Desde esta carpeta:

```bash
git init
git add .
git commit -m "feat: estructura inicial MVC y conexión MySQL"
git add .
git commit -m "feat: gestión de productos y movimientos de inventario"
git add .
git commit -m "test: pruebas unitarias de control de stock"
```

Crea un repositorio vacío en GitHub y enlázalo:

```bash
git branch -M main
git remote add origin https://github.com/TU-USUARIO/TU-REPOSITORIO.git
git push -u origin main
```

No inventes capturas ni commits: sube el proyecto y conserva evidencia real del historial y las pruebas.

## Limitaciones de esta versión
- La administración de usuarios no se persiste en MySQL.
- No incluye CRUD web completo para categorías/proveedores ni exportación Excel, aunque la base y dependencias están preparadas.
- El endpoint REST de creación es básico; el flujo principal de registro se realiza mediante la interfaz web.
- Debes probarlo en tu equipo y ajustar credenciales/versión de Java según tu instalación antes de entregarlo.
