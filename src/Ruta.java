import java.util.Arrays;

public class Ruta {
    private int numero;
    private String clasificacion;
    private double longitud;
    private String[] provincias;

    public Ruta(int numero, String clasificacion, double longitud, String[] provincias) {
        this.numero = numero;
        this.clasificacion = clasificacion;
        this.longitud = longitud;
        this.provincias = provincias;
    }

    public int getNumero() {
        return numero;
    }

    public String getClasificacion() {
        return clasificacion;
    }

    public double getLongitud() {
        return longitud;
    }

    public String[] getProvincias() {
        return provincias;
    }

    @Override
    public String toString() {
        return "Ruta{" +
                "numero=" + numero +
                ", clasificacion='" + clasificacion + '\'' +
                ", longitud=" + longitud +
                ", provincias=" + Arrays.toString(provincias) +
                '}';
    }
}