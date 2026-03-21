package archivo;

import modelo.Producto;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ReporteExcel {

    private static final String RUTA = "reportes";
 private static final String C_HDR_BG   = "1F4E79"; 
    private static final String C_HDR_FG   = "FFFFFF"; 
    private static final String C_TOTAL_BG = "FFD700"; 
    private static final String C_ALT      = "F2F2F2";  

    private static final Map<String, String> CAT_COLOR = new LinkedHashMap<>();
    static {
        CAT_COLOR.put("Abarrotes",               "D5E8D4");
        CAT_COLOR.put("Bebidas",                 "DAE8FC");
        CAT_COLOR.put("Lácteos y Huevo",         "FFF2CC");
        CAT_COLOR.put("Frutas y Verduras",       "C8E6C9");
        CAT_COLOR.put("Carnes y Pescados",       "FFE0B2");
        CAT_COLOR.put("Salchichonería",          "FFCDD2");
        CAT_COLOR.put("Panadería y Tortillería", "E1BEE7");
        CAT_COLOR.put("Limpieza del Hogar",      "B3E5FC");
        CAT_COLOR.put("Cuidado Personal",        "FFF9C4");
        CAT_COLOR.put("Snacks y Dulcería",       "FFE0B2");
        CAT_COLOR.put("Mascotas",                "DCEDC8");
    }
  public static String reporteListadoProductos(List<Producto> productos) throws IOException {
        new File(RUTA).mkdirs();
        String ruta = RUTA + File.separator + "listado_productos.xlsx";
  List<Producto> sorted = new ArrayList<>(productos);
        sorted.sort(Comparator.comparing(Producto::getCategoria)
                              .thenComparing(Producto::getCodigo));

        List<String[]> filas = new ArrayList<>();
        filas.add(new String[]{"Código","Nombre","Categoría","Precio","Cantidad",
                               "Valor Total","Almacenamiento","Descripción","URL Imagen"});
        for (Producto p : sorted) {
            filas.add(new String[]{
                p.getCodigo(), p.getNombre(), p.getCategoria(),
                String.format("$%.2f", p.getPrecio()),
                String.valueOf(p.getCantidad()),
                String.format("$%.2f", p.totalValor()),
                p.tipoAlmacenamiento(), p.getDescripcion(), p.getImagenUrl()
            });
        }

        double totalPrecio = sorted.stream().mapToDouble(Producto::getPrecio).sum();
        int    totalCant   = sorted.stream().mapToInt(Producto::getCantidad).sum();
        double totalVal    = sorted.stream().mapToDouble(Producto::totalValor).sum();
        filas.add(new String[]{
            "TOTAL: "+sorted.size()+" productos","","",
            String.format("$%.2f", totalPrecio),
            String.valueOf(totalCant),
            String.format("$%.2f", totalVal),
            "","",""
        });

        List<String[]> estilos = construirEstilosListado(sorted);

        Map<String, String> sheets = new LinkedHashMap<>();
        sheets.put("Listado de Productos", buildSheet(filas, estilos,
            "LISTADO GENERAL DE PRODUCTOS – SUPERMERCADO", 9,
            new int[]{10,30,22,12,10,13,26,35,55}));

        generarXlsx(ruta, sheets);
        System.out.println("✔ Reporte generado: " + ruta);
        return ruta;
    }
public static String reportePorCategoria(List<Producto> productos) throws IOException {
        new File(RUTA).mkdirs();
        String ruta = RUTA + File.separator + "listado_por_categoria.xlsx";

        Map<String, List<Producto>> grupos = new LinkedHashMap<>();
         List<String> cats = new ArrayList<>();
        for (Producto p : productos)
            if (!cats.contains(p.getCategoria())) cats.add(p.getCategoria());
        Collections.sort(cats);
        for (String cat : cats) {
            List<Producto> grp = new ArrayList<>();
            for (Producto p : productos)
                if (p.getCategoria().equals(cat)) grp.add(p);
            grp.sort(Comparator.comparing(Producto::getCodigo));
            grupos.put(cat, grp);
        }

        Map<String, String> sheets = new LinkedHashMap<>();

        List<String[]> resFilas = new ArrayList<>();
        resFilas.add(new String[]{"Categoría","# Productos","Valor Total Inventario","Tipo Almacenamiento"});
        for (Map.Entry<String, List<Producto>> e : grupos.entrySet()) {
            double total = e.getValue().stream().mapToDouble(Producto::totalValor).sum();
            resFilas.add(new String[]{
                e.getKey(),
                String.valueOf(e.getValue().size()),
                String.format("$%.2f", total),
                e.getValue().get(0).tipoAlmacenamiento()
            });
        }
        List<String[]> resEstilos = new ArrayList<>();
        resEstilos.add(new String[]{"H","H","H","H"});
        for (int i = 1; i < resFilas.size(); i++) {
            String cat  = resFilas.get(i)[0];
            String bg   = CAT_COLOR.getOrDefault(cat, "FFFFFF");
            resEstilos.add(new String[]{"C"+bg,"C"+bg,"C"+bg,"C"+bg});
        }
        sheets.put("Resumen", buildSheet(resFilas, resEstilos,
            "RESUMEN POR CATEGORÍA", 4, new int[]{28,14,24,28}));
 for (Map.Entry<String, List<Producto>> e : grupos.entrySet()) {
            String cat   = e.getKey();
            List<Producto> grp = e.getValue();
            String color = CAT_COLOR.getOrDefault(cat, "FFFFFF");

            List<String[]> filas = new ArrayList<>();
            filas.add(new String[]{"Código","Nombre","Precio","Cantidad","Valor Total","Almacenamiento","URL Imagen"});
            for (Producto p : grp) {
                filas.add(new String[]{
                    p.getCodigo(), p.getNombre(),
                    String.format("$%.2f", p.getPrecio()),
                    String.valueOf(p.getCantidad()),
                    String.format("$%.2f", p.totalValor()),
                    p.tipoAlmacenamiento(), p.getImagenUrl()
                });
            }
            double subtotal = grp.stream().mapToDouble(Producto::totalValor).sum();
            filas.add(new String[]{"SUBTOTAL ("+grp.size()+" productos)","","","",
                                   String.format("$%.2f", subtotal),"",""});

            List<String[]> est = new ArrayList<>();
            est.add(new String[]{"H","H","H","H","H","H","H"});
            for (int i = 1; i <= grp.size(); i++) {
                String bg = (i % 2 == 0) ? "C"+color : "CN";
                est.add(new String[]{bg,bg,bg,bg,bg,bg,bg});
            }
            est.add(new String[]{"T","T","T","T","T","T","T"});

            String nombreHoja = cat.length() > 31 ? cat.substring(0,31) : cat;
            sheets.put(nombreHoja, buildSheet(filas, est,
                "CATEGORÍA: " + cat.toUpperCase(), 7,
                new int[]{12,32,12,10,13,26,55}));
        }

        generarXlsx(ruta, sheets);
        System.out.println("✔ Reporte generado: " + ruta);
        return ruta;
    }
  public static String reporteControlTickets(List<String[]> tickets) throws IOException {
        new File(RUTA).mkdirs();
        String ruta = RUTA + File.separator + "control_tickets.xlsx";

        List<String[]> filas = new ArrayList<>();
        filas.add(new String[]{"Folio","Fecha","Hora","Cliente","Nombre","# Artículos","Total"});
        double grandTotal = 0;
        for (String[] t : tickets) {
            filas.add(t);
            try { grandTotal += Double.parseDouble(t[5]); } catch(Exception ignored){}
        }
        if (!tickets.isEmpty())
            filas.add(new String[]{"TOTAL TICKETS: "+tickets.size(),"","","","","",
                                   String.format("$%.2f", grandTotal)});

        List<String[]> estilos = new ArrayList<>();
        estilos.add(new String[]{"H","H","H","H","H","H","H"});
        for (int i = 1; i <= tickets.size(); i++) {
            String bg = (i%2==0) ? "C"+C_ALT : "CN";
            estilos.add(new String[]{bg,bg,bg,bg,bg,bg,bg});
        }
        if (!tickets.isEmpty())
            estilos.add(new String[]{"T","T","T","T","T","T","T"});

        Map<String,String> sheets = new LinkedHashMap<>();
        sheets.put("Control de Tickets", buildSheet(filas, estilos,
            "CONTROL DE TICKETS DE VENTA", 7,
            new int[]{22,12,10,12,22,12,14}));

        generarXlsx(ruta, sheets);
        System.out.println("✔ Reporte generado: " + ruta);
        return ruta;
    }

    private static List<String[]> construirEstilosListado(List<Producto> sorted) {
        List<String[]> est = new ArrayList<>();
        est.add(new String[]{"H","H","H","H","H","H","H","H","H"});
        for (int i = 0; i < sorted.size(); i++) {
            String bg = CAT_COLOR.getOrDefault(sorted.get(i).getCategoria(), "FFFFFF");
            String e  = (i%2==0) ? "C"+bg : "CN";
            est.add(new String[]{e,e,e,e,e,e,e,e,e});
        }
        est.add(new String[]{"T","T","T","T","T","T","T","T","T"});
        return est;
    }
   private static String buildSheet(List<String[]> filas, List<String[]> estilos,
                                     String titulo, int numCols, int[] anchos) {
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
        xml.append("<worksheet xmlns=\"http://schemas.openxmlformats.org/spreadsheetml/2006/main\">\n");
 xml.append("<cols>\n");
        for (int i = 0; i < anchos.length; i++)
            xml.append(String.format("<col min=\"%d\" max=\"%d\" width=\"%d\" customWidth=\"1\"/>\n",
                       i+1, i+1, anchos[i]));
        xml.append("</cols>\n");
        xml.append("<sheetData>\n");

        int excelRow = 1;

        xml.append(String.format("<row r=\"%d\"><c r=\"%s%d\" s=\"5\" t=\"s\"><v>%d</v></c></row>\n",
                   excelRow, colLetter(1), excelRow, addSharedString(titulo)));
        excelRow++;

        String fechaGen = "Generado: " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
        xml.append(String.format("<row r=\"%d\"><c r=\"%s%d\" s=\"1\" t=\"s\"><v>%d</v></c></row>\n",
                   excelRow, colLetter(1), excelRow, addSharedString(fechaGen)));
        excelRow++;

    
        for (int r = 0; r < filas.size(); r++, excelRow++) {
            String[] fila  = filas.get(r);
            String[] estF  = (r < estilos.size()) ? estilos.get(r) : new String[]{};
            xml.append("<row r=\"").append(excelRow).append("\">\n");
            for (int c = 0; c < fila.length; c++) {
                String celda = fila[c] == null ? "" : fila[c];
                String est   = (c < estF.length) ? estF[c] : "CN";
                int sIdx     = estiloIdx(est);
                xml.append(String.format("<c r=\"%s%d\" s=\"%d\" t=\"s\"><v>%d</v></c>",
                           colLetter(c+1), excelRow, sIdx, addSharedString(celda)));
            }
            xml.append("\n</row>\n");
        }

        xml.append("</sheetData>\n</worksheet>");
        return xml.toString();
    }
  private static final List<String> sharedStrings = new ArrayList<>();
    private static int addSharedString(String s) {
        int idx = sharedStrings.indexOf(s);
        if (idx >= 0) return idx;
        sharedStrings.add(s);
        return sharedStrings.size() - 1;
    }
    private static String buildSharedStrings() {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
        sb.append("<sst xmlns=\"http://schemas.openxmlformats.org/spreadsheetml/2006/main\" count=\"")
          .append(sharedStrings.size()).append("\" uniqueCount=\"").append(sharedStrings.size()).append("\">\n");
        for (String s : sharedStrings)
            sb.append("<si><t xml:space=\"preserve\">").append(escXml(s)).append("</t></si>\n");
        sb.append("</sst>");
        return sb.toString();
    }

   private static int estiloIdx(String est) {
        if (est.equals("H"))  return 2;  // header
        if (est.equals("T"))  return 3;  // total
        if (est.equals("CN")) return 0;  // normal
        if (est.startsWith("C")) return 4; // color (todos al mismo idx simplificado)
        return 0;
    }

    private static String buildStyles(Set<String> coloresUsados) {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
        sb.append("<styleSheet xmlns=\"http://schemas.openxmlformats.org/spreadsheetml/2006/main\">\n");

      sb.append("<fonts count=\"5\">\n");
        sb.append("<font><sz val=\"10\"/><name val=\"Calibri\"/></font>\n");                                                
        sb.append("<font><i/><sz val=\"9\"/><name val=\"Calibri\"/></font>\n");                                          
        sb.append("<font><b/><sz val=\"11\"/><color rgb=\"FF"+C_HDR_FG+"\"/><name val=\"Calibri\"/></font>\n");             
        sb.append("<font><b/><sz val=\"10\"/><name val=\"Calibri\"/></font>\n");                                         
        sb.append("<font><b/><sz val=\"14\"/><color rgb=\"FF"+C_HDR_FG+"\"/><name val=\"Calibri\"/></font>\n");         
        sb.append("</fonts>\n");

        List<String> fills = new ArrayList<>();
        fills.add("<fill><patternFill patternType=\"none\"/></fill>");
        fills.add("<fill><patternFill patternType=\"gray125\"/></fill>");
        fills.add("<fill><patternFill patternType=\"solid\"><fgColor rgb=\"FF"+C_HDR_BG+"\"/></patternFill></fill>");
        fills.add("<fill><patternFill patternType=\"solid\"><fgColor rgb=\"FF"+C_TOTAL_BG+"\"/></patternFill></fill>");
        fills.add("<fill><patternFill patternType=\"solid\"><fgColor rgb=\"FF"+C_ALT+"\"/></patternFill></fill>");
        for (String col : coloresUsados)
            fills.add("<fill><patternFill patternType=\"solid\"><fgColor rgb=\"FF"+col+"\"/></patternFill></fill>");

        sb.append("<fills count=\"").append(fills.size()).append("\">\n");
        for (String f : fills) sb.append(f).append("\n");
        sb.append("</fills>\n");
 sb.append("<borders count=\"2\">\n");
        sb.append("<border><left/><right/><top/><bottom/><diagonal/></border>\n");
        sb.append("<border>\n");
        sb.append("<left style=\"thin\"><color rgb=\"FFBDBDBD\"/></left>\n");
        sb.append("<right style=\"thin\"><color rgb=\"FFBDBDBD\"/></right>\n");
        sb.append("<top style=\"thin\"><color rgb=\"FFBDBDBD\"/></top>\n");
        sb.append("<bottom style=\"thin\"><color rgb=\"FFBDBDBD\"/></bottom>\n");
        sb.append("<diagonal/></border>\n");
        sb.append("</borders>\n");

        sb.append("<cellStyleXfs count=\"1\"><xf numFmtId=\"0\" fontId=\"0\" fillId=\"0\" borderId=\"0\"/></cellStyleXfs>\n");
  sb.append("<cellXfs count=\"6\">\n");
        sb.append("<xf numFmtId=\"0\" fontId=\"0\" fillId=\"0\" borderId=\"1\" xfId=\"0\"><alignment wrapText=\"1\" vertical=\"center\"/></xf>\n");  // 0
        sb.append("<xf numFmtId=\"0\" fontId=\"1\" fillId=\"0\" borderId=\"0\" xfId=\"0\"><alignment horizontal=\"right\"/></xf>\n");               // 1
        sb.append("<xf numFmtId=\"0\" fontId=\"2\" fillId=\"2\" borderId=\"1\" xfId=\"0\"><alignment horizontal=\"center\" vertical=\"center\" wrapText=\"1\"/></xf>\n"); // 2 header
        sb.append("<xf numFmtId=\"0\" fontId=\"3\" fillId=\"3\" borderId=\"1\" xfId=\"0\"><alignment horizontal=\"center\" vertical=\"center\"/></xf>\n"); // 3 total
        sb.append("<xf numFmtId=\"0\" fontId=\"0\" fillId=\"4\" borderId=\"1\" xfId=\"0\"><alignment wrapText=\"1\" vertical=\"center\"/></xf>\n");   // 4 color alt
        sb.append("<xf numFmtId=\"0\" fontId=\"4\" fillId=\"2\" borderId=\"1\" xfId=\"0\"><alignment horizontal=\"center\" vertical=\"center\"/></xf>\n"); // 5 título
        sb.append("</cellXfs>\n");

        sb.append("</styleSheet>");
        return sb.toString();
    }

  private static void generarXlsx(String ruta, Map<String,String> sheets) throws IOException {
        sharedStrings.clear();

       Map<String, String> sheetXmls = new LinkedHashMap<>();
        for (Map.Entry<String, String> e : sheets.entrySet())
            sheetXmls.put(e.getKey(), e.getValue());

        Set<String> coloresUsados = new LinkedHashSet<>(CAT_COLOR.values());
        coloresUsados.add(C_ALT);

       try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(ruta))) {
            zos.setMethod(ZipOutputStream.DEFLATED);

            zip(zos, "_rels/.rels",
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<Relationships xmlns=\"http://schemas.openxmlformats.org/package/2006/relationships\">\n" +
                "<Relationship Id=\"rId1\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument\" Target=\"xl/workbook.xml\"/>\n" +
                "</Relationships>");

            StringBuilder ct = new StringBuilder();
            ct.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
            ct.append("<Types xmlns=\"http://schemas.openxmlformats.org/package/2006/content-types\">\n");
            ct.append("<Default Extension=\"rels\" ContentType=\"application/vnd.openxmlformats-package.relationships+xml\"/>\n");
            ct.append("<Default Extension=\"xml\" ContentType=\"application/xml\"/>\n");
            ct.append("<Override PartName=\"/xl/workbook.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet.main+xml\"/>\n");
            ct.append("<Override PartName=\"/xl/sharedStrings.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.spreadsheetml.sharedStrings+xml\"/>\n");
            ct.append("<Override PartName=\"/xl/styles.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.spreadsheetml.styles+xml\"/>\n");
            int si = 1;
            for (String ignored : sheetXmls.keySet())
                ct.append("<Override PartName=\"/xl/worksheets/sheet").append(si++).append(".xml\" ContentType=\"application/vnd.openxmlformats-officedocument.spreadsheetml.worksheet+xml\"/>\n");
            ct.append("</Types>");
            zip(zos, "[Content_Types].xml", ct.toString());

            StringBuilder wbRels = new StringBuilder();
            wbRels.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
            wbRels.append("<Relationships xmlns=\"http://schemas.openxmlformats.org/package/2006/relationships\">\n");
            si = 1;
            for (String ignored : sheetXmls.keySet()) {
                wbRels.append("<Relationship Id=\"rId").append(si)
                      .append("\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/worksheet\" Target=\"worksheets/sheet")
                      .append(si).append(".xml\"/>\n");
                si++;
            }
            wbRels.append("<Relationship Id=\"rId").append(si)
                  .append("\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/sharedStrings\" Target=\"sharedStrings.xml\"/>\n");
            si++;
            wbRels.append("<Relationship Id=\"rId").append(si)
                  .append("\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/styles\" Target=\"styles.xml\"/>\n");
            wbRels.append("</Relationships>");
            zip(zos, "xl/_rels/workbook.xml.rels", wbRels.toString());

            // xl/workbook.xml
            StringBuilder wb = new StringBuilder();
            wb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
            wb.append("<workbook xmlns=\"http://schemas.openxmlformats.org/spreadsheetml/2006/main\" ");
            wb.append("xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\">\n");
            wb.append("<sheets>\n");
            si = 1;
            for (String nombre : sheetXmls.keySet()) {
                wb.append("<sheet name=\"").append(escXml(nombre))
                  .append("\" sheetId=\"").append(si)
                  .append("\" r:id=\"rId").append(si).append("\"/>\n");
                si++;
            }
            wb.append("</sheets>\n</workbook>");
            zip(zos, "xl/workbook.xml", wb.toString());

            si = 1;
            for (String xml : sheetXmls.values()) {
                zip(zos, "xl/worksheets/sheet" + si + ".xml", xml);
                si++;
            }

            zip(zos, "xl/sharedStrings.xml", buildSharedStrings());

            zip(zos, "xl/styles.xml", buildStyles(coloresUsados));
        }
    }

    private static void zip(ZipOutputStream zos, String nombre, String contenido) throws IOException {
        zos.putNextEntry(new ZipEntry(nombre));
        zos.write(contenido.getBytes(StandardCharsets.UTF_8));
        zos.closeEntry();
    }

   static String colLetter(int col) {
        StringBuilder sb = new StringBuilder();
        while (col > 0) {
            col--;
            sb.insert(0, (char)('A' + col % 26));
            col /= 26;
        }
        return sb.toString();
    }

    static String escXml(String s) {
        return s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;")
                .replace("\"","&quot;").replace("'","&apos;");
    }
}
