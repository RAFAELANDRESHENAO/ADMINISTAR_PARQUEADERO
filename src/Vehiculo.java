import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

// Clase Base - Vehiculo
abstract class Vehiculo {
    private String placa;
    private String marca;
    private String modelo;
    private LocalDateTime horaEntrada;

    public Vehiculo(String placa, String marca, String modelo, LocalDateTime horaEntrada) {
        this.placa = placa.toUpperCase(); // Convertir placa a mayúsculas
        this.marca = marca;
        this.modelo = modelo;
        this.horaEntrada = horaEntrada;
    }

    public String getPlaca() {
        return placa;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public LocalDateTime getHoraEntrada() {
        return horaEntrada;
    }

    // Método abstracto para calcular la tarifa
    public abstract double calcularTarifa(LocalDateTime horaSalida);

    // Método para calcular el tiempo de estancia en minutos
    protected long calcularTiempoEstancia(LocalDateTime horaSalida) {
        return Duration.between(horaEntrada, horaSalida).toMinutes();
    }
}

// Clase derivada - Carro
class Carro extends Vehiculo {
    private String tipoCombustible;

    public Carro(String placa, String marca, String modelo, LocalDateTime horaEntrada, String tipoCombustible) {
        super(placa, marca, modelo, horaEntrada);
        this.tipoCombustible = tipoCombustible;
    }

    @Override
    public double calcularTarifa(LocalDateTime horaSalida) {
        long minutos = calcularTiempoEstancia(horaSalida);
        return Math.ceil(minutos / 60.0) * 100; // Tarifa de 100 pesos por minuto
    }
}

// Clase derivada - Moto
class Moto extends Vehiculo {
    private int cilindraje;

    public Moto(String placa, String marca, String modelo, LocalDateTime horaEntrada, int cilindraje) {
        super(placa, marca, modelo, horaEntrada);
        this.cilindraje = cilindraje;
    }

    @Override
    public double calcularTarifa(LocalDateTime horaSalida) {
        long minutos = calcularTiempoEstancia(horaSalida);
        return Math.ceil(minutos / 60.0) * 80; // Tarifa de 80 pesos por minuto
    }
}

// Clase derivada - Camión
class Camion extends Vehiculo {
    private double capacidadCarga;

    public Camion(String placa, String marca, String modelo, LocalDateTime horaEntrada, double capacidadCarga) {
        super(placa, marca, modelo, horaEntrada);
        this.capacidadCarga = capacidadCarga;
    }

    @Override
    public double calcularTarifa(LocalDateTime horaSalida) {
        long minutos = calcularTiempoEstancia(horaSalida);
        return Math.ceil(minutos / 60.0) * 200; // Tarifa de 200 pesos por minuto
    }
}

// Clase derivada - Bicicleta
class Bicicleta extends Vehiculo {
    public Bicicleta(String placa, String marca, String modelo, LocalDateTime horaEntrada) {
        super(placa, marca, modelo, horaEntrada);
    }

    @Override
    public double calcularTarifa(LocalDateTime horaSalida) {
        return 0; // Sin costo para bicicletas
    }
}

// Clase Parqueadero
class Parqueadero {
    private List<Vehiculo> vehiculos;
    private List<String> reporteDiario;

    public Parqueadero() {
        vehiculos = new ArrayList<>();
        reporteDiario = new ArrayList<>();
    }

    public void registrarEntrada(Vehiculo vehiculo) {
        vehiculos.add(vehiculo);
        reporteDiario.add("Entrada: " + vehiculo.getPlaca() + " - " + vehiculo.getMarca() + " - " + vehiculo.getModelo() + " - " + vehiculo.getHoraEntrada());
    }

    public double registrarSalida(String placa) {
        for (Vehiculo v : vehiculos) {
            if (v.getPlaca().equals(placa)) {
                vehiculos.remove(v);
                LocalDateTime horaSalida = LocalDateTime.now();
                double tarifa = v.calcularTarifa(horaSalida);
                reporteDiario.add("Salida: " + placa + " - Costo: " + tarifa);
                return tarifa;
            }
        }
        return -1; // Vehículo no encontrado
    }

    public List<Vehiculo> consultarEstado() {
        return new ArrayList<>(vehiculos); // Retorna una copia de la lista
    }

    public List<String> generarReporteDiario() {
        return new ArrayList<>(reporteDiario); // Retorna una copia del reporte
    }
}