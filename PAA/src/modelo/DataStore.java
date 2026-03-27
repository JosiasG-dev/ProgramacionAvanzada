package modelo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DataStore {
	private static DataStore instancia;

	private List<Producto> productos = new ArrayList<>();
	private List<Proveedor> proveedores = new ArrayList<>();
	private List<MovimientoInventario> movimientos = new ArrayList<>();
	private List<UnidadMedida> unidades = new ArrayList<>();
	private List<Venta> ventas = new ArrayList<>();

	private int contadorProductos = 1;
	private int contadorProveedores = 1;
	private int contadorMovimientos = 1;
	private int contadorUnidades = 1;
	private int contadorVentas = 1;

	private DataStore() {
		inicializarDatosDemo();
	}

	public static DataStore getInstance() {
		if (instancia == null)
			instancia = new DataStore();
		return instancia;
	}

	private void inicializarDatosDemo() {
		unidades.add(new UnidadMedida(contadorUnidades++, "Pieza", "pza", "Unidad"));
		unidades.add(new UnidadMedida(contadorUnidades++, "Kilogramo", "kg", "Peso"));
		unidades.add(new UnidadMedida(contadorUnidades++, "Gramo", "g", "Peso"));
		unidades.add(new UnidadMedida(contadorUnidades++, "Litro", "L", "Volumen"));
		unidades.add(new UnidadMedida(contadorUnidades++, "Mililitro", "mL", "Volumen"));
		unidades.add(new UnidadMedida(contadorUnidades++, "Caja", "caja", "Unidad"));
		unidades.add(new UnidadMedida(contadorUnidades++, "Bolsa", "bolsa", "Unidad"));
		unidades.add(new UnidadMedida(contadorUnidades++, "Paquete", "paq", "Unidad"));
		unidades.add(new UnidadMedida(contadorUnidades++, "Docena", "doc", "Unidad"));
		unidades.add(new UnidadMedida(contadorUnidades++, "Metro", "m", "Longitud"));

		// --- Proveedores ---
		proveedores.add(new Proveedor(contadorProveedores++, "Distribuidora Norte S.A.", "Juan Pérez", "867-123-4567",
				"ventas@distnorte.mx", "Av. Industrial 100, Monterrey", "DNS020101AAA"));
		proveedores.add(new Proveedor(contadorProveedores++, "Mayoreo Tamaulipas", "María López", "834-987-6543",
				"contacto@mayoreo-tam.mx", "Calle Comercio 45, Tampico", "MAT030215BBB"));
		proveedores.add(new Proveedor(contadorProveedores++, "Lácteos del Valle", "Carlos Ruiz", "868-555-1234",
				"pedidos@lacteosvalle.mx", "Blvd. Agroindustrial 22, Victoria", "LDV010101CCC"));
		proveedores.add(new Proveedor(contadorProveedores++, "Bebidas Express MX", "Ana Torres", "812-333-9999",
				"ventas@bebidaex.mx", "Zona Industrial, Matamoros", "BEM050610DDD"));

		// --- Productos ---
		// Despensa Básica
		agregarProductoDemo("Arroz Morelos 1kg", "Despensa Básica", "Arroz, frijol y leguminosas",
				new BigDecimal("12.00"), 35, 50, "kg",
				"https://www.surti-tienda.mx/media/catalog/product/a/r/arroz-morelos.jpg");
		agregarProductoDemo("Frijol Negro 1kg", "Despensa Básica", "Arroz, frijol y leguminosas",
				new BigDecimal("22.00"), 30, 40, "kg",
				"https://www.surti-tienda.mx/media/catalog/product/f/r/frijol-negro.jpg");
		agregarProductoDemo("Aceite Nutrioli 946mL", "Despensa Básica", "Aceites y grasas", new BigDecimal("45.00"), 25,
				30, "pza", "https://www.surti-tienda.mx/media/catalog/product/a/c/aceite-nutrioli.jpg");
		agregarProductoDemo("Sopa de Pasta Maruchan", "Despensa Básica", "Pastas y sopas", new BigDecimal("8.00"), 40,
				60, "pza", "https://www.surti-tienda.mx/media/catalog/product/s/o/sopa-maruchan.jpg");
		agregarProductoDemo("Harina Maseca 1kg", "Despensa Básica", "Harinas y repostería", new BigDecimal("18.00"), 30,
				35, "pza", "https://www.surti-tienda.mx/media/catalog/product/h/a/harina-maseca.jpg");
		agregarProductoDemo("Azúcar Estándar 1kg", "Despensa Básica", "Azúcar y endulzantes", new BigDecimal("20.00"),
				25, 45, "kg", "https://www.surti-tienda.mx/media/catalog/product/a/z/azucar-estandar.jpg");

		// Lácteos y Huevo
		agregarProductoDemo("Leche Lala Entera 1L", "Lácteos y Huevo", "Leche", new BigDecimal("18.00"), 20, 24, "pza",
				"https://www.surti-tienda.mx/media/catalog/product/l/e/leche-lala-entera.jpg");
		agregarProductoDemo("Huevo Blanco Bachoco 12pz", "Lácteos y Huevo", "Huevos", new BigDecimal("38.00"), 20, 20,
				"pza", "https://www.surti-tienda.mx/media/catalog/product/h/u/huevo-bachoco.jpg");
		agregarProductoDemo("Queso Panela Lala 400g", "Lácteos y Huevo", "Quesos frescos y maduros",
				new BigDecimal("45.00"), 25, 12, "pza",
				"https://www.surti-tienda.mx/media/catalog/product/q/u/queso-panela.jpg");

		// Bebidas y Líquidos
		agregarProductoDemo("Agua Ciel 600mL", "Bebidas y Líquidos", "Agua natural y saborizada",
				new BigDecimal("5.00"), 50, 48, "pza",
				"https://www.surti-tienda.mx/media/catalog/product/a/g/agua-ciel.jpg");
		agregarProductoDemo("Coca-Cola 600mL", "Bebidas y Líquidos", "Refrescos y sodas", new BigDecimal("12.00"), 35,
				36, "pza", "https://www.surti-tienda.mx/media/catalog/product/c/o/coca-cola-600.jpg");
		agregarProductoDemo("Jugo Del Valle Naranja 1L", "Bebidas y Líquidos", "Jugos y néctares",
				new BigDecimal("22.00"), 30, 24, "pza",
				"https://www.surti-tienda.mx/media/catalog/product/j/u/jugo-delvalle.jpg");

		// Botanas y Dulces
		agregarProductoDemo("Sabritas Clásicas 45g", "Botanas y Dulces", "Papas y frituras", new BigDecimal("10.00"),
				40, 30, "pza", "https://www.surti-tienda.mx/media/catalog/product/s/a/sabritas-clasicas.jpg");
		agregarProductoDemo("Galletas Oreo 36g", "Botanas y Dulces", "Galletas dulces y saladas",
				new BigDecimal("8.00"), 35, 40, "pza",
				"https://www.surti-tienda.mx/media/catalog/product/g/a/galletas-oreo.jpg");
		agregarProductoDemo("Chocolate Carlos V 16g", "Botanas y Dulces", "Dulces y chocolates", new BigDecimal("6.00"),
				40, 50, "pza", "https://www.surti-tienda.mx/media/catalog/product/c/h/chocolate-carlosv.jpg");

		// Cuidado del Hogar
		agregarProductoDemo("Detergente Ariel 1kg", "Cuidado del Hogar", "Detergentes y suavizantes",
				new BigDecimal("55.00"), 25, 20, "pza",
				"https://www.surti-tienda.mx/media/catalog/product/d/e/detergente-ariel.jpg");
		agregarProductoDemo("Papel Higiénico Regio 4 rollos", "Cuidado del Hogar", "Papel higiénico y servilletas",
				new BigDecimal("28.00"), 30, 25, "pza",
				"https://www.surti-tienda.mx/media/catalog/product/p/a/papel-regio.jpg");

		// Higiene y Cuidado Personal
		agregarProductoDemo("Jabón Dove 90g", "Higiene y Cuidado Personal", "Jabones y geles de baño",
				new BigDecimal("15.00"), 35, 30, "pza",
				"https://www.surti-tienda.mx/media/catalog/product/j/a/jabon-dove.jpg");
		agregarProductoDemo("Shampoo Head&Shoulders 375mL", "Higiene y Cuidado Personal",
				"Champú y cuidado del cabello", new BigDecimal("58.00"), 25, 18, "pza",
				"https://www.surti-tienda.mx/media/catalog/product/s/h/shampoo-hs.jpg");

		// Alimentos Preparados/Enlatados
		agregarProductoDemo("Atún Dolores Agua 130g", "Alimentos Preparados", "Atún y sardinas",
				new BigDecimal("18.00"), 30, 35, "pza",
				"https://www.surti-tienda.mx/media/catalog/product/a/t/atun-dolores.jpg");
		agregarProductoDemo("Sopa Maruchan Pollo", "Alimentos Preparados", "Sopas instantáneas", new BigDecimal("9.50"),
				40, 50, "pza", "https://www.surti-tienda.mx/media/catalog/product/s/o/sopa-maruchan-pollo.jpg");
	}

	private void agregarProductoDemo(String nombre, String categoria, String subcategoria, BigDecimal precioCompra,
			double ganancia, int cantidad, String unidad, String imgUrl) {
		Producto p = new Producto(contadorProductos++, nombre, categoria, subcategoria, precioCompra, ganancia,
				cantidad, unidad, imgUrl);
		productos.add(p);
	}

	public List<Producto> getProductos() {
		return new ArrayList<>(productos);
	}

	public List<Producto> getProductosActivos() {
		return productos.stream().filter(Producto::isActivo).collect(Collectors.toList());
	}

	public Producto getProductoPorId(int id) {
		return productos.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
	}

	public void agregarProducto(Producto producto) {
		producto.setId(contadorProductos++);
		productos.add(producto);
	}

	public void actualizarProducto(Producto producto) {
		for (int i = 0; i < productos.size(); i++) {
			if (productos.get(i).getId() == producto.getId()) {
				productos.set(i, producto);
				return;
			}
		}
	}

	public void eliminarProducto(int id) {
		productos.removeIf(p -> p.getId() == id);
	}

	public List<String> getCategorias() {
		return productos.stream().map(Producto::getCategoria).distinct().sorted().collect(Collectors.toList());
	}

	public List<Proveedor> getProveedores() {
		return new ArrayList<>(proveedores);
	}

	public void agregarProveedor(Proveedor proveedor) {
		proveedor.setId(contadorProveedores++);
		proveedores.add(proveedor);
	}

	public void actualizarProveedor(Proveedor proveedor) {
		for (int i = 0; i < proveedores.size(); i++) {
			if (proveedores.get(i).getId() == proveedor.getId()) {
				proveedores.set(i, proveedor);
				return;
			}
		}
	}

	public void eliminarProveedor(int id) {
		proveedores.removeIf(p -> p.getId() == id);
	}

	public List<UnidadMedida> getUnidades() {
		return new ArrayList<>(unidades);
	}

	public void agregarUnidad(UnidadMedida unidad) {
		unidad.setId(contadorUnidades++);
		unidades.add(unidad);
	}

	public void actualizarUnidad(UnidadMedida unidad) {
		for (int i = 0; i < unidades.size(); i++) {
			if (unidades.get(i).getId() == unidad.getId()) {
				unidades.set(i, unidad);
				return;
			}
		}
	}

	public void eliminarUnidad(int id) {
		unidades.removeIf(u -> u.getId() == id);
	}

	public List<MovimientoInventario> getMovimientos() {
		return new ArrayList<>(movimientos);
	}

	public void registrarMovimiento(MovimientoInventario mov) {
		mov.setId(contadorMovimientos++);
		movimientos.add(mov);
		Producto p = getProductoPorId(mov.getProductoId());
		if (p != null) {
			if (mov.getTipo() == MovimientoInventario.TipoMovimiento.ENTRADA) {
				p.setCantidadAlmacen(p.getCantidadAlmacen() + mov.getCantidad());
			} else if (mov.getTipo() == MovimientoInventario.TipoMovimiento.SALIDA) {
				p.setCantidadAlmacen(p.getCantidadAlmacen() - mov.getCantidad());
			} else {
				p.setCantidadAlmacen(mov.getCantidad());
			}
		}
	}

	public List<Venta> getVentas() {
		return new ArrayList<>(ventas);
	}

	public void registrarVenta(Venta venta) {
		venta.setId(contadorVentas);
		venta.setFolio(String.format("V-%05d", contadorVentas++));
		ventas.add(venta);
		for (DetalleVenta det : venta.getDetalles()) {
			Producto p = getProductoPorId(det.getProductoId());
			if (p != null) {
				p.setCantidadAlmacen(p.getCantidadAlmacen() - det.getCantidad());
			}
		}
	}
}
