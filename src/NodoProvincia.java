import java.util.ArrayList;

public class NodoProvincia {
    private String nombre;
    private ArrayList<Ruta> rutas;
    private NodoProvincia izquierda, derecha;

    public NodoProvincia(String nombre) {
        this.nombre = nombre;
        this.rutas = new ArrayList<>();
        this.izquierda = this.derecha = null;
    }

    public String getNombre() {
        return nombre;
    }

    public ArrayList<Ruta> getRutas() {
        return rutas;
    }

    public void agregarRuta(Ruta ruta) {
        rutas.add(ruta);
    }

    public NodoProvincia getIzquierda() {
        return izquierda;
    }

    public void setIzquierda(NodoProvincia izquierda) {
        this.izquierda = izquierda;
    }

    public NodoProvincia getDerecha() {
        return derecha;
    }

    public void setDerecha(NodoProvincia derecha) {
        this.derecha = derecha;
    }
}