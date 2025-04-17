package teatroMoro;

import java.util.Scanner;

public class TeatroMoro3 {

    static int totalEntradasVendidas = 0;
    static double totalIngresos = 0;
    static int totalReservas = 0;
    static boolean errorControl = false;

    static String estado1 = "LIBRE", estado2 = "LIBRE", estado3 = "LIBRE", estado4 = "LIBRE", estado5 = "LIBRE";
    static String tipoCliente1 = "", tipoCliente2 = "", tipoCliente3 = "", tipoCliente4 = "", tipoCliente5 = "";
    static double precio1 = 0, precio2 = 0, precio3 = 0, precio4 = 0, precio5 = 0;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean salir = false;

        do {
            try {
                mostrarMenu();
                String entrada = sc.nextLine();
                if (entrada.isEmpty())
                    throw new IllegalArgumentException("Entrada vacia");
                int opcion = Integer.parseInt(entrada);
                errorControl = false;

                switch (opcion) {
                    case 1 -> reservarAsiento(sc);
                    case 2 -> comprarAsiento(sc);
                    case 3 -> modificarVenta(sc);
                    case 4 -> imprimirBoleta(sc);
                    case 5 -> salir = true;
                    default -> System.out.println("Opcion no valida.");
                }
            } catch (Exception e) {
                errorControl = true;
                System.out.println("Error inesperado: " + e.getMessage());
            }
        } while (!salir);

        System.out.println("Gracias por usar el sistema del Teatro Moro.");
        sc.close();
    }

    public static void mostrarMenu() {
        System.out.println("\n--- MENU PRINCIPAL ---");
        System.out.println("1. Reservar asiento");
        System.out.println("2. Comprar asiento");
        System.out.println("3. Modificar venta");
        System.out.println("4. Imprimir boleta");
        System.out.println("5. Salir");
        System.out.print("Seleccione una opcion: ");
    }

    public static void mostrarEstadoAsientos() {
        System.out.println("\n--- ESTADO DE ASIENTOS ---");
        System.out.println("1. VIP             - " + estado1);
        System.out.println("2. PLATEA ALTA     - " + estado2);
        System.out.println("3. PLATEA BAJA     - " + estado3);
        System.out.println("4. GENERAL         - " + estado4);
        System.out.println("5. GENERAL2        - " + estado5);
    }

    public static void reservarAsiento(Scanner sc) {
        mostrarEstadoAsientos();
        System.out.print("Seleccione numero de asiento a reservar: ");
        int asiento = Integer.parseInt(sc.nextLine());

        switch (asiento) {
            case 1 -> estado1 = validarReserva(estado1);
            case 2 -> estado2 = validarReserva(estado2);
            case 3 -> estado3 = validarReserva(estado3);
            case 4 -> estado4 = validarReserva(estado4);
            case 5 -> estado5 = validarReserva(estado5);
            default -> System.out.println("Asiento invalido.");
        }
    }

    public static String validarReserva(String estado) {
        if (estado.equals("LIBRE")) {
            totalReservas++;
            System.out.println("Reserva realizada exitosamente.");
            return "RESERVADO";
        } else {
            System.out.println("Asiento no disponible para reserva.");
            return estado;
        }
    }

    public static void comprarAsiento(Scanner sc) {
        mostrarEstadoAsientos();
        System.out.print("Seleccione numero de asiento a comprar: ");
        int asiento = Integer.parseInt(sc.nextLine());
        System.out.print("Tipo de cliente (Estudiante, Tercera Edad, General): ");
        String tipo = sc.nextLine();

        double precioBase = 15000;
        double descuento = tipo.equalsIgnoreCase("Estudiante") ? 0.10
                : tipo.equalsIgnoreCase("Tercera Edad") ? 0.15 : 0;
        double precioFinal = precioBase - (precioBase * descuento);

        registrarVenta(tipo, precioFinal, asiento);
    }

    public static void registrarVenta(String tipo, double precio, int asiento) {
        switch (asiento) {
            case 1 -> actualizarAsiento("VIP", estado1, tipo, precio, a -> estado1 = a, b -> tipoCliente1 = b,
                    c -> precio1 = c);
            case 2 -> actualizarAsiento("PLATEA ALTA", estado2, tipo, precio, a -> estado2 = a, b -> tipoCliente2 = b,
                    c -> precio2 = c);
            case 3 -> actualizarAsiento("PLATEA BAJA", estado3, tipo, precio, a -> estado3 = a, b -> tipoCliente3 = b,
                    c -> precio3 = c);
            case 4 -> actualizarAsiento("GENERAL", estado4, tipo, precio, a -> estado4 = a, b -> tipoCliente4 = b,
                    c -> precio4 = c);
            case 5 -> actualizarAsiento("GENERAL2", estado5, tipo, precio, a -> estado5 = a, b -> tipoCliente5 = b,
                    c -> precio5 = c);
            default -> System.out.println("Numero de asiento no reconocido.");
        }
    }

    public static void actualizarAsiento(String zona, String estado, String tipo, double precio,
            java.util.function.Consumer<String> setEstado,
            java.util.function.Consumer<String> setTipo,
            java.util.function.DoubleConsumer setPrecio) {
        if (!estado.equals("COMPRADO")) {
            setEstado.accept("COMPRADO");
            setTipo.accept(tipo);
            setPrecio.accept(precio);
            totalEntradasVendidas++;
            totalIngresos += precio;
            System.out.println("Venta registrada (" + zona + "). Precio final: $" + precio);
        } else {
            System.out.println("Asiento ya comprado.");
        }
    }

    public static void modificarVenta(Scanner sc) {
        System.out.print("Ingrese numero de asiento para modificar: ");
        int asiento = Integer.parseInt(sc.nextLine());
        System.out.print("Nuevo tipo de cliente: ");
        String tipo = sc.nextLine();

        double precioBase = 15000;
        double descuento = tipo.equalsIgnoreCase("Estudiante") ? 0.10
                : tipo.equalsIgnoreCase("Tercera Edad") ? 0.15 : 0;
        double nuevoPrecio = precioBase - (precioBase * descuento);

        switch (asiento) {
            case 1 -> aplicarModificacion(estado1, tipo, nuevoPrecio, v -> tipoCliente1 = v, p -> precio1 = p);
            case 2 -> aplicarModificacion(estado2, tipo, nuevoPrecio, v -> tipoCliente2 = v, p -> precio2 = p);
            case 3 -> aplicarModificacion(estado3, tipo, nuevoPrecio, v -> tipoCliente3 = v, p -> precio3 = p);
            case 4 -> aplicarModificacion(estado4, tipo, nuevoPrecio, v -> tipoCliente4 = v, p -> precio4 = p);
            case 5 -> aplicarModificacion(estado5, tipo, nuevoPrecio, v -> tipoCliente5 = v, p -> precio5 = p);
            default -> System.out.println("Asiento invalido para modificar.");
        }
    }

    public static void aplicarModificacion(String estado, String tipo, double nuevoPrecio,
            java.util.function.Consumer<String> setTipo,
            java.util.function.DoubleConsumer setPrecio) {
        if (estado.equals("COMPRADO")) {
            setTipo.accept(tipo);
            setPrecio.accept(nuevoPrecio);
            System.out.println("Modificacion aplicada correctamente.");
        } else {
            System.out.println("El asiento no ha sido comprado previamente.");
        }
    }

    public static void imprimirBoleta(Scanner sc) {
        System.out.print("Ingrese numero de asiento: ");
        int asiento = Integer.parseInt(sc.nextLine());

        switch (asiento) {
            case 1 -> mostrarBoleta("VIP", tipoCliente1, precio1, estado1);
            case 2 -> mostrarBoleta("PLATEA ALTA", tipoCliente2, precio2, estado2);
            case 3 -> mostrarBoleta("PLATEA BAJA", tipoCliente3, precio3, estado3);
            case 4 -> mostrarBoleta("GENERAL", tipoCliente4, precio4, estado4);
            case 5 -> mostrarBoleta("GENERAL2", tipoCliente5, precio5, estado5);
            default -> System.out.println("Asiento invalido.");
        }
    }

    public static void mostrarBoleta(String zona, String tipo, double precio, String estado) {
        if (estado.equals("COMPRADO")) {
            System.out.println("\n--- BOLETA ---");
            System.out.println("Zona: " + zona);
            System.out.println("Tipo de Cliente: " + tipo);
            System.out.println("Precio: $" + precio);
        } else {
            System.out.println("El asiento no ha sido comprado.");
        }
    }
}
