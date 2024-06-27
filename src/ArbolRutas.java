public class ArbolRutas {
    private NodoProvincia raiz;

    public ArbolRutas() {
        this.raiz = null;
    }

    public void insertar(String nombre) {
        raiz = insertarRecursivo(raiz, nombre);
    }

    private NodoProvincia insertarRecursivo(NodoProvincia actual, String nombre) {
        if (actual == null) {
            return new NodoProvincia(nombre);
        }

        if (nombre.compareTo(actual.getNombre()) < 0) {
            actual.setIzquierda(insertarRecursivo(actual.getIzquierda(), nombre));
        } else if (nombre.compareTo(actual.getNombre()) > 0) {
            actual.setDerecha(insertarRecursivo(actual.getDerecha(), nombre));
        }

        return actual;
    }

    public NodoProvincia buscar(String nombre) {
        return buscarRecursivo(raiz, nombre);
    }

    private NodoProvincia buscarRecursivo(NodoProvincia actual, String nombre) {
        if (actual == null || actual.getNombre().equals(nombre)) {
            return actual;
        }

        if (nombre.compareTo(actual.getNombre()) < 0) {
            return buscarRecursivo(actual.getIzquierda(), nombre);
        } else {
            return buscarRecursivo(actual.getDerecha(), nombre);
        }
    }
}