package teatroMoro;

import java.util.*;

public class TeatroMoro2 {

    // Variables estaticas
    static String nombreTeatro = "Teatro Moro";
    static int capacidad = 100;
    static int totalVendidas = 0;
    static double totalIngresos = 0;
    static Map<String, Set<Integer>> mapaZonas = new HashMap<>(); // zonas con asientos ocupados

    // Lista de entradas vendidas
    static List<Entrada> entradasVendidas = new ArrayList<>();
    static final String[] ZONAS_VALIDAS = {"VIP", "PLATEA ALTA", "PLATEA BAJA", "GENERAL"};

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean salir = false;

        // Inicializar zonas
        for (String zona : ZONAS_VALIDAS) {
            mapaZonas.put(zona.toUpperCase(), new HashSet<>());
        }

        while (!salir) {
            System.out.println("\n--- MENU PRINCIPAL ---");
            System.out.println("1. Venta de entrada");
            System.out.println("2. Ver promociones");
            System.out.println("3. Buscar entrada");
            System.out.println("4. Eliminar entrada");
            System.out.println("5. Estadisticas");
            System.out.println("6. Salir");
            System.out.print("Seleccione una opcion: ");
            String opcion = sc.nextLine();

            switch (opcion) {
                case "1" -> venderEntrada(sc);
                case "2" -> mostrarPromociones();
                case "3" -> buscarEntrada(sc);
                case "4" -> eliminarEntrada(sc);
                case "5" -> mostrarEstadisticas();
                case "6" -> salir = true;
                default -> System.out.println("Opcion no valida.");
            }
        }

        sc.close();
        System.out.println("Gracias por usar el sistema del " + nombreTeatro);
    }

    public static void venderEntrada(Scanner sc) {
        // Variables locales temporales
        String zona;
        int numero;
        String tipo;
        int edad;
        String detalleCompra;

        System.out.print("Ingrese zona (VIP, Platea Alta, Platea Baja, General): ");
        zona = sc.nextLine().toUpperCase();

        if (!mapaZonas.containsKey(zona)) {
            System.out.println("Zona invalida. Operacion cancelada.");
            return;
        }

        System.out.print("Cuantas entradas desea comprar?: ");
        int cantidad = Integer.parseInt(sc.nextLine());

        System.out.print("Tipo de cliente (Estudiante, Tercera Edad, General): ");
        tipo = sc.nextLine();

        System.out.print("Ingrese edad del cliente: ");
        edad = Integer.parseInt(sc.nextLine());

        double precio = 15000;
        double descuento = 0;
        int entradasGratis = 0;
        String textoDescuento = "Sin descuento";

        if (tipo.equalsIgnoreCase("Estudiante")) {
            descuento = 0.10;
            textoDescuento = "10% Estudiante";
        } else if (tipo.equalsIgnoreCase("Tercera Edad")) {
            descuento = 0.15;
            textoDescuento = "15% Tercera Edad";
            if (cantidad >= 3) {
                entradasGratis = cantidad / 3; // 3x2
                System.out.println("Promocion 3x2 aplicada: " + entradasGratis + " entradas gratis.");
            }
        } else if (tipo.equalsIgnoreCase("General") && zona.equalsIgnoreCase("GENERAL") && cantidad >= 25) {
            entradasGratis = 5;
            System.out.println("Promocion curso aplicada: 5 entradas gratis por 25 compradas.");
        }

        int totalEntradas = cantidad + entradasGratis;
        double precioTotal = precio * cantidad;
        double totalDescuento = precioTotal * descuento;
        double totalFinal = precioTotal - totalDescuento;

        // Registro de todas las entradas
        for (int i = 0; i < totalEntradas; i++) {
            System.out.print("Ingrese numero de asiento para entrada #" + (i + 1) + ": ");
            numero = Integer.parseInt(sc.nextLine());

            if (!mapaZonas.containsKey(zona)) {
                System.out.println("Zona invalida. Operacion cancelada.");
                return;
            }

            if (mapaZonas.get(zona).contains(numero)) {
                System.out.println("Asiento ocupado. Intente con otro.");
                i--;
                continue;
            }

            mapaZonas.get(zona).add(numero);
            Entrada nueva = new Entrada(numero, zona, tipo, edad, (i < cantidad ? (precio - (precio * descuento)) : 0), textoDescuento);
            entradasVendidas.add(nueva);
        }

        totalVendidas += totalEntradas;
        totalIngresos += totalFinal;

        detalleCompra = "Compra registrada: " + totalEntradas + " entradas. Total pagado: $" + totalFinal + " (" + textoDescuento + ")";
        System.out.println(detalleCompra);
    }

    public static void mostrarPromociones() {
        System.out.println("\n--- PROMOCIONES DISPONIBLES ---");
        System.out.println("- 10% de descuento para estudiantes");
        System.out.println("- 15% de descuento para tercera edad");
        System.out.println("- 3x2 para tercera edad (al comprar 3 entradas)");
        System.out.println("- Curso: por 25 entradas General, lleva 5 gratis");
    }

    public static void buscarEntrada(Scanner sc) {
        System.out.print("Ingrese zona (VIP, Platea Alta, Platea Baja, General): ");
        String zonaBuscada = sc.nextLine().toUpperCase();
        System.out.print("Buscar por numero de asiento: ");
        int numero = Integer.parseInt(sc.nextLine());

        boolean encontrada = false;
        for (Entrada e : entradasVendidas) {
            if (e.numeroAsiento == numero && e.zona.equalsIgnoreCase(zonaBuscada)) {
                System.out.println(e);
                encontrada = true;
                break;
            }
        }
        if (!encontrada) {
            System.out.println("No se encontro ninguna entrada con ese numero en la zona indicada.");
        }
    }

    public static void eliminarEntrada(Scanner sc) {
        System.out.print("Ingrese numero de asiento a eliminar: ");
        int numero = Integer.parseInt(sc.nextLine());

        Iterator<Entrada> it = entradasVendidas.iterator();
        while (it.hasNext()) {
            Entrada e = it.next();
            if (e.numeroAsiento == numero) {
                it.remove();
                totalVendidas--;
                totalIngresos -= e.precioFinal;
                mapaZonas.get(e.zona).remove(e.numeroAsiento);
                System.out.println("Entrada eliminada correctamente.");
                return;
            }
        }
        System.out.println("No se encontro entrada con ese numero.");
    }

    public static void mostrarEstadisticas() {
        System.out.println("\n--- ESTADISTICAS ---");
        System.out.println("Entradas vendidas: " + totalVendidas);
        System.out.println("Ingresos totales: $" + totalIngresos);
    }
}

class Entrada {
    // Variables de instancia
    int numeroAsiento;
    String zona;
    String tipoCliente;
    int edad;
    double precioFinal;
    String detalleDescuento;

    public Entrada(int numeroAsiento, String zona, String tipoCliente, int edad, double precioFinal, String detalleDescuento) {
        this.numeroAsiento = numeroAsiento;
        this.zona = zona;
        this.tipoCliente = tipoCliente;
        this.edad = edad;
        this.precioFinal = precioFinal;
        this.detalleDescuento = detalleDescuento;
    }

    @Override
    public String toString() {
        return "Asiento: " + numeroAsiento + ", Zona: " + zona + ", Cliente: " + tipoCliente + ", Edad: " + edad + ", Precio Final: $" + precioFinal + ", Descuento: " + detalleDescuento;
    }
}
