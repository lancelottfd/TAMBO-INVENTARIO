CREATE DATABASE IF NOT EXISTS tambo_inventario CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE tambo_inventario;

INSERT INTO categorias (nombre, descripcion, activa)
SELECT 'Bebidas', 'Aguas, gaseosas y bebidas', 1
WHERE NOT EXISTS (SELECT 1 FROM categorias WHERE nombre = 'Bebidas');

INSERT INTO categorias (nombre, descripcion, activa)
SELECT 'Snacks', 'Galletas, papas y aperitivos', 1
WHERE NOT EXISTS (SELECT 1 FROM categorias WHERE nombre = 'Snacks');

INSERT INTO categorias (nombre, descripcion, activa)
SELECT 'Abarrotes', 'Productos de consumo diario', 1
WHERE NOT EXISTS (SELECT 1 FROM categorias WHERE nombre = 'Abarrotes');

INSERT INTO proveedores (nombre, ruc, telefono, correo, direccion, activo)
SELECT 'Proveedor de prueba', '20123456789', '999999999', 'proveedor@example.com', 'Arequipa, Perú', 1
WHERE NOT EXISTS (SELECT 1 FROM proveedores WHERE nombre = 'Proveedor de prueba');
