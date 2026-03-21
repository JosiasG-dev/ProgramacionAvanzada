package archivo;

import modelo.*;

import java.io.*;
import java.util.ArrayList;

public class ArchivoJSON {
	public static final String RUTA_PRODUCTOS = "archivo" + File.separator + "productos.json";
	public static final String RUTA_TICKETS = "tickets";
	public static final String RUTA_CONTROL = RUTA_TICKETS + File.separator + "_control_tickets.json";

	public static void exportarProductos(ArrayList<Producto> lista) {
		new File("archivo").mkdirs();
		StringBuilder sb = new StringBuilder("[\n");
		for (int i = 0; i < lista.size(); i++) {
			sb.append("  ").append(lista.get(i).toJson());
			if (i < lista.size() - 1)
				sb.append(",");
			sb.append("\n");
		}
		sb.append("]");
		escribir(RUTA_PRODUCTOS, sb.toString());
		System.out.println("✔ Catálogo guardado: " + RUTA_PRODUCTOS + " (" + lista.size() + " productos)");
	}

	public static ArrayList<Producto> importarProductos() {
		ArrayList<Producto> lista = new ArrayList<>();
		File f = new File(RUTA_PRODUCTOS);
		if (!f.exists())
			return lista;

		String json = leer(f);
		ArrayList<String> objetos = extraerObjetos(json);
		for (String obj : objetos) {
			try {
				Producto p = parsearProducto(obj);
				if (p != null)
					lista.add(p);
			} catch (Exception e) {
				System.err.println("⚠ Error al parsear producto: " + e.getMessage());
			}
		}
		System.out.println("✔ " + lista.size() + " productos cargados desde " + RUTA_PRODUCTOS);
		return lista;
	}

	public static String guardarTicket(modelo.Ticket ticket) {
		new File(RUTA_TICKETS).mkdirs();
		String ruta = RUTA_TICKETS + File.separator + ticket.getFolio() + ".json";
		escribir(ruta, ticket.toJson());
		actualizarControlTickets(ticket);
		System.out.println("✔ Ticket guardado: " + ruta);
		return ruta;
	}

	public static ArrayList<String[]> cargarControlTickets() {
		ArrayList<String[]> lista = new ArrayList<>();
		File f = new File(RUTA_CONTROL);
		if (!f.exists())
			return lista;
		String json = leer(f);
		ArrayList<String> objetos = extraerObjetos(json);
		for (String obj : objetos) {
			String folio = getStr(obj, "folio");
			String fecha = getStr(obj, "fecha");
			String hora = getStr(obj, "hora");
			String cliente = getStr(obj, "cliente");
			String nombre = getStr(obj, "nombreCliente");
			String total = getStr(obj, "total");
			String nItems = getStr(obj, "numItems");
			lista.add(new String[] { folio, fecha, hora, cliente, nombre, total, nItems });
		}
		return lista;
	}

	private static void actualizarControlTickets(modelo.Ticket t) {
		ArrayList<String[]> lista = cargarControlTickets();
		StringBuilder entry = new StringBuilder();
		entry.append("  {\n");
		entry.append("    \"folio\":").append(q(t.getFolio())).append(",\n");
		entry.append("    \"fecha\":").append(q(t.getFecha())).append(",\n");
		entry.append("    \"hora\":").append(q(t.getHora())).append(",\n");
		entry.append("    \"cliente\":").append(q(t.getCliente())).append(",\n");
		entry.append("    \"nombreCliente\":").append(q(t.getNombreCliente())).append(",\n");
		entry.append("    \"total\":").append(t.getTotal()).append(",\n");
		entry.append("    \"numItems\":").append(t.getNumItems()).append("\n");
		entry.append("  }");

		File f = new File(RUTA_CONTROL);
		String json = f.exists() ? leer(f).trim() : "[]";
		int idx = json.lastIndexOf(']');
		String prevContent = json.substring(0, idx).trim();
		String nuevo;
		if (prevContent.equals("[")) {
			nuevo = "[\n" + entry + "\n]";
		} else {
			nuevo = prevContent + ",\n" + entry + "\n]";
		}
		escribir(RUTA_CONTROL, nuevo);
	}

	private static Producto parsearProducto(String json) {
		String categoria = getStr(json, "categoria");
		String codigo = getStr(json, "codigo");
		String nombre = getStr(json, "nombre");
		double precio = getDouble(json, "precio");
		int cantidad = (int) getDouble(json, "cantidad");
		String imagenUrl = getStr(json, "imagenUrl");
		String desc = getStr(json, "descripcion");

		switch (categoria) {
		case "Abarrotes":
			return new ProductoAbarrotes(codigo, nombre, precio, cantidad, getDouble(json, "pesoKg"), imagenUrl, desc);
		case "Bebidas":
			return new ProductoBebidas(codigo, nombre, precio, cantidad, (int) getDouble(json, "volumenMl"),
					getBool(json, "alcoholica"), imagenUrl, desc);
		case "Lácteos y Huevo":
			return new ProductoLacteos(codigo, nombre, precio, cantidad, (int) getDouble(json, "diasCaducidad"),
					imagenUrl, desc);
		case "Frutas y Verduras":
			return new ProductoFrutasVerduras(codigo, nombre, precio, cantidad, getStr(json, "unidadVenta"),
					getStr(json, "temporada"), imagenUrl, desc);
		case "Carnes y Pescados":
			return new ProductoCarnesPescados(codigo, nombre, precio, cantidad, getStr(json, "tipoCorte"),
					getBool(json, "congelado"), imagenUrl, desc);
		case "Salchichonería":
			return new ProductoSalchichoneria(codigo, nombre, precio, cantidad, getBool(json, "ventaPorKilo"),
					imagenUrl, desc);
		case "Panadería y Tortillería":
			return new ProductoPanaderia(codigo, nombre, precio, cantidad, (int) getDouble(json, "piezasPorPaquete"),
					imagenUrl, desc);
		case "Limpieza del Hogar":
			return new ProductoLimpieza(codigo, nombre, precio, cantidad, getBool(json, "contieneQuimicos"), imagenUrl,
					desc);
		case "Cuidado Personal":
			return new ProductoCuidadoPersonal(codigo, nombre, precio, cantidad, getStr(json, "tipoPiel"), imagenUrl,
					desc);
		case "Snacks y Dulcería":
			return new ProductoSnacks(codigo, nombre, precio, cantidad, getStr(json, "sabor"), imagenUrl, desc);
		case "Mascotas":
			return new ProductoMascotas(codigo, nombre, precio, cantidad, getStr(json, "especie"),
					getDouble(json, "pesoBolsaKg"), imagenUrl, desc);
		default:
			System.err.println("⚠ Categoría desconocida: " + categoria);
			return null;
		}
	}

	static ArrayList<String> extraerObjetos(String json) {
		ArrayList<String> lista = new ArrayList<>();
		int nivel = 0, inicio = -1;
		for (int i = 0; i < json.length(); i++) {
			char c = json.charAt(i);
			if (c == '{') {
				if (nivel == 0)
					inicio = i;
				nivel++;
			} else if (c == '}') {
				nivel--;
				if (nivel == 0 && inicio >= 0) {
					lista.add(json.substring(inicio, i + 1));
					inicio = -1;
				}
			}
		}
		return lista;
	}

	static String getStr(String json, String clave) {
		String buscar = "\"" + clave + "\"";
		int idx = json.indexOf(buscar);
		if (idx < 0)
			return "";
		int colon = json.indexOf(':', idx + buscar.length());
		if (colon < 0)
			return "";
		int q1 = json.indexOf('"', colon + 1);
		if (q1 < 0)
			return "";
		int q2 = q1 + 1;
		while (q2 < json.length()) {
			if (json.charAt(q2) == '"' && json.charAt(q2 - 1) != '\\')
				break;
			q2++;
		}
		return json.substring(q1 + 1, q2).replace("\\\"", "\"");
	}

	static double getDouble(String json, String clave) {
		String buscar = "\"" + clave + "\"";
		int idx = json.indexOf(buscar);
		if (idx < 0)
			return 0;
		int colon = json.indexOf(':', idx + buscar.length());
		if (colon < 0)
			return 0;
		StringBuilder num = new StringBuilder();
		for (int i = colon + 1; i < json.length(); i++) {
			char c = json.charAt(i);
			if (Character.isDigit(c) || c == '.' || c == '-')
				num.append(c);
			else if (num.length() > 0)
				break;
		}
		return num.length() > 0 ? Double.parseDouble(num.toString()) : 0;
	}

	static boolean getBool(String json, String clave) {
		String buscar = "\"" + clave + "\"";
		int idx = json.indexOf(buscar);
		if (idx < 0)
			return false;
		int colon = json.indexOf(':', idx + buscar.length());
		if (colon < 0)
			return false;
		String resto = json.substring(colon + 1).trim();
		return resto.startsWith("true");
	}

	private static void escribir(String ruta, String contenido) {
		try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(ruta), "UTF-8"))) {
			bw.write(contenido);
		} catch (IOException e) {
			System.err.println("Error escribiendo " + ruta + ": " + e.getMessage());
		}
	}

	private static String leer(File f) {
		StringBuilder sb = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"))) {
			String linea;
			while ((linea = br.readLine()) != null)
				sb.append(linea).append("\n");
		} catch (IOException e) {
			System.err.println("Error leyendo " + f + ": " + e.getMessage());
		}
		return sb.toString();
	}

	private static String q(String s) {
		return "\"" + s.replace("\"", "\\\"") + "\"";
	}
}
