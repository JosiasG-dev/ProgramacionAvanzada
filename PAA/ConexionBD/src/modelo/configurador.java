package modelo;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class configurador {
    public static Map<String, String> cargarConfiguracion() {
        Map<String, String> config = new HashMap<>();
        try {
            File archivo = new File("configuracion.xml");
            if (!archivo.exists()) {
                throw new Exception("No se encuentra: " + archivo.getAbsolutePath());
            }
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(archivo);
            doc.getDocumentElement().normalize();

            String motorActivo = doc.getElementsByTagName("motor_activo")
                                    .item(0).getTextContent();
            config.put("motor", motorActivo);

            NodeList perfiles = doc.getElementsByTagName("db_perfil");
            for (int i = 0; i < perfiles.getLength(); i++) {
                Element perfil = (Element) perfiles.item(i);
                if (perfil.getAttribute("id").equals(motorActivo)) {
                    config.put("host",   perfil.getElementsByTagName("host").item(0).getTextContent());
                    config.put("puerto", perfil.getElementsByTagName("puerto").item(0).getTextContent());
                    config.put("db",     perfil.getElementsByTagName("nombre_bd").item(0).getTextContent());
                    config.put("user",   perfil.getElementsByTagName("usuario").item(0).getTextContent());
                    config.put("pass",   perfil.getElementsByTagName("password").item(0).getTextContent());
                }
            }
        } catch (Exception e) {
            System.err.println("Error leyendo XML: " + e.getMessage());
        }
        return config;
    }
}
