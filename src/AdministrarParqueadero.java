import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;


// Interfaz de Usuario para Administrar  Parqueadero
public class AdministrarParqueadero extends JFrame {
    private JTextField placaField;
    private JTextField marcaField;
    private JTextField modeloField;
    private JComboBox<String> tipoCombustibleComboBox;
    private JTextField cilindrajeField;
    private JTextField capacidadCargaField;
    private JTextArea estadoArea;
    private JComboBox<String> tipoVehiculoComboBox;
    private Parqueadero parqueadero;

    public AdministrarParqueadero() {
        // Configurar la ventana
        setTitle(" PARQUEADERO MONTECARLO");
        setSize(1000, 500); // Tamaño ajustado
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Inicializar el parqueadero
        parqueadero = new Parqueadero();

        // Crear y configurar el panel de contenido
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Espaciado entre componentes
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Añadir JComboBox para tipo de vehículo
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER; // Ocupa toda la fila
        tipoVehiculoComboBox = new JComboBox<>(new String[] { "Selecciona tipo de vehículo", "Carro", "Moto", "Camión", "Bicicleta" });
        panel.add(tipoVehiculoComboBox, gbc);

        // Añadir JComboBox para tipo de combustible
        gbc.gridy++;
        gbc.gridwidth = GridBagConstraints.REMAINDER; // Ocupa toda la fila
        tipoCombustibleComboBox = new JComboBox<>(new String[] { "Selecciona tipo de combustible", "Gasolina", "ACPM", "Gas", "Eléctrico" });
        panel.add(tipoCombustibleComboBox, gbc);

        // Añadir etiquetas y campos de texto
        gbc.gridwidth = 1; // Resetea la gridwidth para los siguientes campos
        gbc.gridy++;
        addLabel(panel, "Placa:", gbc);
        placaField = addTextField(panel, gbc);

        gbc.gridy++;
        addLabel(panel, "Marca:", gbc);
        marcaField = addTextField(panel, gbc);

        gbc.gridy++;
        addLabel(panel, "Modelo:", gbc);
        modeloField = addTextField(panel, gbc);

        gbc.gridy++;
        addLabel(panel, "Cilindraje:", gbc);
        cilindrajeField = addTextField(panel, gbc);

        gbc.gridy++;
        addLabel(panel, "Capacidad Carga:", gbc);
        capacidadCargaField = addTextField(panel, gbc);

        // Botones
        gbc.gridy++;
        gbc.gridwidth = GridBagConstraints.REMAINDER; // Ocupa toda la fila
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton registrarEntradaButton = new JButton("Registrar Entrada");
        JButton registrarSalidaButton = new JButton("Registrar Salida");
        JButton consultarEstadoButton = new JButton("Consultar Estado");
        JButton refrescarButton = new JButton("Refrescar");
        JButton reporteDiarioButton = new JButton("Generar Reporte Diario");

        buttonPanel.add(registrarEntradaButton);
        buttonPanel.add(registrarSalidaButton);
        buttonPanel.add(consultarEstadoButton);
        buttonPanel.add(refrescarButton);
        buttonPanel.add(reporteDiarioButton);

        panel.add(buttonPanel, gbc);

        // Área de texto para mostrar el estado
        estadoArea = new JTextArea(10, 40);
        estadoArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(estadoArea);

        gbc.gridy++;
        gbc.gridwidth = GridBagConstraints.REMAINDER; // Ocupa toda la fila
        panel.add(scrollPane, gbc);

        // Añadir panel al frame
        add(panel);

        // Acción del botón registrar entrada
        registrarEntradaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String placa = placaField.getText().toUpperCase(); // Convertir a mayúsculas
                String marca = marcaField.getText();
                String modelo = modeloField.getText();
                LocalDateTime horaEntrada = LocalDateTime.now();
                String tipoVehiculo = (String) tipoVehiculoComboBox.getSelectedItem();
                String tipoCombustible = (String) tipoCombustibleComboBox.getSelectedItem();
                String cilindrajeText = cilindrajeField.getText();
                String capacidadCargaText = capacidadCargaField.getText();

                // Validar campos vacíos
                if (placa.isEmpty() || marca.isEmpty() || modelo.isEmpty() || tipoVehiculo.equals("Selecciona tipo de vehículo")) {
                    JOptionPane.showMessageDialog(AdministrarParqueadero.this, "Todos los campos deben ser completados.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Validar formato de la placa
                boolean placaValida = false;
                if (tipoVehiculo.equals("Carro") || tipoVehiculo.equals("Camión")) {
                    placaValida = Pattern.matches("[A-Z]{3}\\s[0-9]{3}", placa);
                } else if (tipoVehiculo.equals("Moto")) {
                    placaValida = Pattern.matches("[A-Z]{3}\\s[0-9]{2}[A-Z]", placa);
                } else if (tipoVehiculo.equals("Bicicleta")) {
                    placaValida = Pattern.matches("[0-9]{5}", placa);
                }

                if (!placaValida) {
                    JOptionPane.showMessageDialog(AdministrarParqueadero.this, "Formato de placa inválido.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    Vehiculo vehiculo = null;
                    if (tipoVehiculo.equals("Carro")) {
                        if (tipoCombustible.equals("Selecciona tipo de combustible")) {
                            JOptionPane.showMessageDialog(AdministrarParqueadero.this, "Seleccione un tipo de combustible.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        vehiculo = new Carro(placa, marca, modelo, horaEntrada, tipoCombustible);
                    } else if (tipoVehiculo.equals("Moto")) {
                        if (cilindrajeText.isEmpty()) {
                            JOptionPane.showMessageDialog(AdministrarParqueadero.this, "Ingrese el cilindraje.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        int cilindraje = Integer.parseInt(cilindrajeText);
                        vehiculo = new Moto(placa, marca, modelo, horaEntrada, cilindraje);
                    } else if (tipoVehiculo.equals("Camión")) {
                        if (capacidadCargaText.isEmpty()) {
                            JOptionPane.showMessageDialog(AdministrarParqueadero.this, "Ingrese la capacidad de carga.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        double capacidadCarga = Double.parseDouble(capacidadCargaText);
                        vehiculo = new Camion(placa, marca, modelo, horaEntrada, capacidadCarga);
                    } else if (tipoVehiculo.equals("Bicicleta")) {
                        vehiculo = new Bicicleta(placa, marca, modelo, horaEntrada);
                    } else {
                        JOptionPane.showMessageDialog(AdministrarParqueadero.this, "Seleccione un tipo de vehículo.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    parqueadero.registrarEntrada(vehiculo);
                    estadoArea.append("Entrada registrada: " + placa + "\n");

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(AdministrarParqueadero.this, "Ingrese datos válidos para cilindraje o capacidad de carga.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(AdministrarParqueadero.this, "Error al registrar la entrada: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Acción del botón registrar salida
        registrarSalidaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String placa = placaField.getText().toUpperCase(); // Convertir a mayúsculas
                double tarifa = parqueadero.registrarSalida(placa);

                if (tarifa == -1) {
                    JOptionPane.showMessageDialog(AdministrarParqueadero.this, "Vehículo no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    estadoArea.append("Salida registrada para placa " + placa + ". Costo: " + tarifa + " pesos.\n");
                }
            }
        });

        // Acción del botón consultar estado
        consultarEstadoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                estadoArea.setText("Estado del parqueadero:\n");
                for (Vehiculo v : parqueadero.consultarEstado()) {
                    estadoArea.append(v.getPlaca() + " - " + v.getMarca() + " - " + v.getModelo() + "\n");
                }
            }
        });

        // Acción del botón refrescar
        refrescarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placaField.setText("");
                marcaField.setText("");
                modeloField.setText("");
                cilindrajeField.setText("");
                capacidadCargaField.setText("");
                tipoVehiculoComboBox.setSelectedIndex(0);
                tipoCombustibleComboBox.setSelectedIndex(0);
            }
        });

        // Acción del botón reporte diario
        reporteDiarioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> reporte = parqueadero.generarReporteDiario();
                estadoArea.setText("Reporte Diario:\n");
                for (String linea : reporte) {
                    estadoArea.append(linea + "\n");
                }
            }
        });
    }

    // Método auxiliar para añadir etiquetas
    private void addLabel(JPanel panel, String text, GridBagConstraints gbc) {
        gbc.gridx = 0;
        JLabel label = new JLabel(text);
        panel.add(label, gbc);
    }

    // Método auxiliar para añadir campos de texto
    private JTextField addTextField(JPanel panel, GridBagConstraints gbc) {
        gbc.gridx = 1;
        JTextField textField = new JTextField(20);
        panel.add(textField, gbc);
        return textField;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdministrarParqueadero().setVisible(true));
    }
}