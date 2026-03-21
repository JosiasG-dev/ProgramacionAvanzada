package modelo;

/**
 * Mascotas — Volumen / Peso.
 * Alimento para perros, Alimento para gatos, Accesorios.
 */
public class ProductoMascotas extends Producto {

    private String especie;       // "Perro", "Gato", "Ave", etc.
    private double pesoBolsaKg;

    public ProductoMascotas(String codigo, String nombre, double precio,
                            int cantidad, String especie, double pesoBolsaKg,
                            String imagenUrl, String descripcion) {
        super(codigo, nombre, precio, cantidad, "Mascotas", imagenUrl, descripcion);
        this.especie     = especie;
        this.pesoBolsaKg = pesoBolsaKg;
    }

    @Override public String tipoAlmacenamiento() { return "Volumen / Peso"; }

    @Override public String infoExtraJson() {
        return "\"especie\":" + jsonStr(especie)
             + ",\"pesoBolsaKg\":" + pesoBolsaKg;
    }

    public String getEspecie()            { return especie; }
    public double getPesoBolsaKg()        { return pesoBolsaKg; }
    public void   setEspecie(String v)    { this.especie     = v; }
    public void   setPesoBolsaKg(double v){ this.pesoBolsaKg = v; }
}
