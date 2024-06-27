import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class Main {
    private static ArrayList<Ruta> rutas = new ArrayList<>();
    private static DefaultTableModel model;
    private static ArbolRutas arbolRutas = new ArbolRutas();

    public static void main(String[] args) {
        cargarDatos();

        JFrame frame = new JFrame("Rutas Nacionales");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Crear componentes para la búsqueda
        JTextField textFieldBusqueda = new JTextField(20);
        JButton buttonBuscar = new JButton("Buscar");

        // Crear componentes para el ordenamiento
        String[] atributos = {"Numero", "Clasificacion", "Longitud", "Provincia"};
        JComboBox<String> comboBoxOrdenarA = new JComboBox<>(atributos);
        JComboBox<String> comboBoxOrdenarB = new JComboBox<>(atributos);
        JButton buttonOrdenar = new JButton("Ordenar");

        // Crear componentes para encontrar caminos
        JTextField textFieldProvinciaOrigen = new JTextField(10);
        JTextField textFieldProvinciaDestino = new JTextField(10);
        JButton buttonCaminoMasCorto = new JButton("Camino más corto");
        JButton buttonCaminoMasLargo = new JButton("Camino más largo");
        JButton buttonCaminoMasProvincias = new JButton("Camino con más provincias");
        JButton buttonRutaMasCorta = new JButton("Ruta más corta");
        JButton buttonCaminosMismaClasificacion = new JButton("Caminos misma clasificación");

        // Crear tabla para mostrar las rutas
        String[] columnNames = {"Numero", "Clasificacion", "Longitud", "Provincias"};
        model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        // Agregar componentes al panel
        JPanel panelBusqueda = new JPanel();
        panelBusqueda.add(new JLabel("Buscar:"));
        panelBusqueda.add(textFieldBusqueda);
        panelBusqueda.add(buttonBuscar);

        JPanel panelOrdenar = new JPanel();
        panelOrdenar.add(new JLabel("Ordenar por:"));
        panelOrdenar.add(comboBoxOrdenarA);
        panelOrdenar.add(comboBoxOrdenarB);
        panelOrdenar.add(buttonOrdenar);

        JPanel panelCaminos = new JPanel();
        panelCaminos.add(new JLabel("Provincia Origen:"));
        panelCaminos.add(textFieldProvinciaOrigen);
        panelCaminos.add(new JLabel("Provincia Destino:"));
        panelCaminos.add(textFieldProvinciaDestino);
        panelCaminos.add(buttonCaminoMasCorto);
        panelCaminos.add(buttonCaminoMasLargo);
        panelCaminos.add(buttonCaminoMasProvincias);
        panelCaminos.add(buttonRutaMasCorta);
        panelCaminos.add(buttonCaminosMismaClasificacion);

        panel.add(panelBusqueda, BorderLayout.NORTH);
        panel.add(panelOrdenar, BorderLayout.CENTER);
        panel.add(panelCaminos, BorderLayout.SOUTH);
        panel.add(scrollPane, BorderLayout.EAST);

        frame.add(panel);
        frame.setVisible(true);

        // Acción para buscar rutas
        buttonBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String busqueda = textFieldBusqueda.getText();
                buscarRutas(busqueda);
            }
        });

        // Acción para ordenar rutas
        buttonOrdenar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String atributoA = (String) comboBoxOrdenarA.getSelectedItem();
                String atributoB = (String) comboBoxOrdenarB.getSelectedItem();
                ordenarRutas(atributoA, atributoB);
            }
        });

        // Acción para encontrar el camino más corto
        buttonCaminoMasCorto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String origen = textFieldProvinciaOrigen.getText();
                String destino = textFieldProvinciaDestino.getText();
                Ruta ruta = caminoMasCorto(origen, destino);
                if (ruta != null) {
                    JOptionPane.showMessageDialog(frame, "Camino más corto: " + ruta);
                } else {
                    JOptionPane.showMessageDialog(frame, "No se encontró camino.");
                }
            }
        });

        // Acción para encontrar el camino más largo
        buttonCaminoMasLargo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String origen = textFieldProvinciaOrigen.getText();
                String destino = textFieldProvinciaDestino.getText();
                Ruta ruta = caminoMasLargo(origen, destino);
                if (ruta != null) {
                    JOptionPane.showMessageDialog(frame, "Camino más largo: " + ruta);
                } else {
                    JOptionPane.showMessageDialog(frame, "No se encontró camino.");
                }
            }
        });

        // Acción para encontrar el camino que atraviese más provincias
        buttonCaminoMasProvincias.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String origen = textFieldProvinciaOrigen.getText();
                String destino = textFieldProvinciaDestino.getText();
                Ruta ruta = caminoMasProvincias(origen, destino);
                if (ruta != null) {
                    JOptionPane.showMessageDialog(frame, "Camino con más provincias: " + ruta);
                } else {
                    JOptionPane.showMessageDialog(frame, "No se encontró camino.");
                }
            }
        });

        // Acción para encontrar la ruta más corta desde una provincia
        buttonRutaMasCorta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String origen = textFieldProvinciaOrigen.getText();
                Ruta ruta = rutaMasCorta(origen);
                if (ruta != null) {
                    JOptionPane.showMessageDialog(frame, "Ruta más corta desde " + origen + ": " + ruta);
                } else {
                    JOptionPane.showMessageDialog(frame, "No se encontró ruta.");
                }
            }
        });

        // Acción para encontrar los caminos de la misma clasificación entre dos provincias
        buttonCaminosMismaClasificacion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String origen = textFieldProvinciaOrigen.getText();
                String destino = textFieldProvinciaDestino.getText();
                String clasificacion = JOptionPane.showInputDialog(frame, "Ingrese la clasificación:");
                ArrayList<Ruta> rutas = caminosMismaClasificacion(origen, destino, clasificacion);
                if (!rutas.isEmpty()) {
                    StringBuilder mensaje = new StringBuilder("Caminos de clasificación " + clasificacion + ":\n");
                    for (Ruta ruta : rutas) {
                        mensaje.append(ruta).append("\n");
                    }
                    JOptionPane.showMessageDialog(frame, mensaje.toString());
                } else {
                    JOptionPane.showMessageDialog(frame, "No se encontraron caminos.");
                }
            }
        });

        // Mostrar todas las rutas en la tabla
        mostrarRutas();
    }

    private static void cargarDatos() {
        try (BufferedReader br = new BufferedReader(new FileReader("src/Rutas_nacionales.csv"))) {
            String line;
            // Omitir la primera línea (encabezados)
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");
                int numero = Integer.parseInt(values[0]);
                String clasificacion = values[1];
                double longitud = Double.parseDouble(values[2]);
                String[] provincias = Arrays.copyOfRange(values, 3, values.length);
                Ruta ruta = new Ruta(numero, clasificacion, longitud, provincias);

                rutas.add(ruta);

                // Agregar ruta al árbol binario
                for (String provincia : provincias) {
                    NodoProvincia nodo = arbolRutas.buscar(provincia);
                    if (nodo == null) {
                        arbolRutas.insertar(provincia);
                        nodo = arbolRutas.buscar(provincia);
                    }
                    nodo.agregarRuta(ruta);
                }
            }
            System.out.println("Datos cargados correctamente. Total de rutas: " + rutas.size());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al cargar el archivo CSV.");
        }
    }

    private static void mostrarRutas() {
        model.setRowCount(0); // Limpiar la tabla
        for (Ruta ruta : rutas) {
            Object[] row = {ruta.getNumero(), ruta.getClasificacion(), ruta.getLongitud(), Arrays.toString(ruta.getProvincias())};
            model.addRow(row);
        }
    }

    private static void buscarRutas(String busqueda) {
        model.setRowCount(0); // Limpiar la tabla
        for (Ruta ruta : rutas) {
            if (Arrays.toString(ruta.getProvincias()).contains(busqueda) || ruta.getClasificacion().contains(busqueda)) {
                Object[] row = {ruta.getNumero(), ruta.getClasificacion(), ruta.getLongitud(), Arrays.toString(ruta.getProvincias())};
                model.addRow(row);
            }
        }
    }

    private static void ordenarRutas(String atributoA, String atributoB) {
        Comparator<Ruta> comparator = (r1, r2) -> 0;
        if (atributoA != null && !atributoA.isEmpty()) {
            switch (atributoA) {
                case "Numero":
                    comparator = Comparator.comparingInt(Ruta::getNumero);
                    break;
                case "Clasificacion":
                    comparator = Comparator.comparing(Ruta::getClasificacion);
                    break;
                case "Longitud":
                    comparator = Comparator.comparingDouble(Ruta::getLongitud);
                    break;
                case "Provincia":
                    comparator = Comparator.comparing(r -> r.getProvincias()[0]);
                    break;
            }
        }
        if (atributoB != null && !atributoB.isEmpty()) {
            switch (atributoB) {
                case "Numero":
                    comparator = comparator.thenComparingInt(Ruta::getNumero);
                    break;
                case "Clasificacion":
                    comparator = comparator.thenComparing(Ruta::getClasificacion);
                    break;
                case "Longitud":
                    comparator = comparator.thenComparingDouble(Ruta::getLongitud);
                    break;
                case "Provincia":
                    comparator = comparator.thenComparing(r -> r.getProvincias()[0]);
                    break;
            }
        }
        rutas.sort(comparator);
        mostrarRutas();
    }

    private static Ruta caminoMasCorto(String provinciaOrigen, String provinciaDestino) {
        NodoProvincia origen = arbolRutas.buscar(provinciaOrigen);
        NodoProvincia destino = arbolRutas.buscar(provinciaDestino);

        if (origen == null || destino == null) {
            return null;
        }

        Ruta rutaMasCorta = null;
        double longitudMinima = Double.MAX_VALUE;

        for (Ruta ruta : origen.getRutas()) {
            if (Arrays.asList(ruta.getProvincias()).contains(provinciaDestino) && ruta.getLongitud() < longitudMinima) {
                rutaMasCorta = ruta;
                longitudMinima = ruta.getLongitud();
            }
        }

        return rutaMasCorta;
    }

    private static Ruta caminoMasLargo(String provinciaOrigen, String provinciaDestino) {
        NodoProvincia origen = arbolRutas.buscar(provinciaOrigen);
        NodoProvincia destino = arbolRutas.buscar(provinciaDestino);

        if (origen == null || destino == null) {
            return null;
        }

        Ruta rutaMasLarga = null;
        double longitudMaxima = 0;

        for (Ruta ruta : origen.getRutas()) {
            if (Arrays.asList(ruta.getProvincias()).contains(provinciaDestino) && ruta.getLongitud() > longitudMaxima) {
                rutaMasLarga = ruta;
                longitudMaxima = ruta.getLongitud();
            }
        }

        return rutaMasLarga;
    }

    private static Ruta caminoMasProvincias(String provinciaOrigen, String provinciaDestino) {
        NodoProvincia origen = arbolRutas.buscar(provinciaOrigen);
        NodoProvincia destino = arbolRutas.buscar(provinciaDestino);

        if (origen == null || destino == null) {
            return null;
        }

        Ruta rutaMasProvincias = null;
        int maxProvincias = 0;

        for (Ruta ruta : origen.getRutas()) {
            if (Arrays.asList(ruta.getProvincias()).contains(provinciaDestino) && ruta.getProvincias().length > maxProvincias) {
                rutaMasProvincias = ruta;
                maxProvincias = ruta.getProvincias().length;
            }
        }

        return rutaMasProvincias;
    }

    private static Ruta rutaMasCorta(String provinciaOrigen) {
        NodoProvincia origen = arbolRutas.buscar(provinciaOrigen);

        if (origen == null) {
            return null;
        }

        Ruta rutaMasCorta = null;
        double longitudMinima = Double.MAX_VALUE;

        for (Ruta ruta : origen.getRutas()) {
            if (ruta.getLongitud() < longitudMinima) {
                rutaMasCorta = ruta;
                longitudMinima = ruta.getLongitud();
            }
        }

        return rutaMasCorta;
    }

    private static ArrayList<Ruta> caminosMismaClasificacion(String provinciaOrigen, String provinciaDestino, String clasificacion) {
        NodoProvincia origen = arbolRutas.buscar(provinciaOrigen);
        NodoProvincia destino = arbolRutas.buscar(provinciaDestino);

        if (origen == null || destino == null) {
            return new ArrayList<>();
        }

        ArrayList<Ruta> rutasMismaClasificacion = new ArrayList<>();

        for (Ruta ruta : origen.getRutas()) {
            if (Arrays.asList(ruta.getProvincias()).contains(provinciaDestino) && ruta.getClasificacion().equalsIgnoreCase(clasificacion)) {
                rutasMismaClasificacion.add(ruta);
            }
        }

        return rutasMismaClasificacion;
    }
}