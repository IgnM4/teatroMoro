package teatroMoro;

import java.util.*;

public class teatroMoro {

    static final int TOTAL_ASIENTOS = 100;
    static final String[] ZONAS = {"A", "B", "C", "D"};
    static final int PRECIO_BASE = 15000;

    // Mapa de asientos ocupados por zona
    static Map<String, Set<Integer>> asientosOcupados = new HashMap<>();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean continuarComprando = true;

        // Inicializar mapa de asientos
        for (String zona : ZONAS) {
            asientosOcupados.put(zona, new HashSet<>());
        }

        for (int i = 0; i < 20 && continuarComprando; i++) {
            System.out.println("\n--- MENU PRINCIPAL ---");
            System.out.println("1. Comprar entradas");
            System.out.println("2. Salir");
            System.out.print("Seleccione una opcion: ");
            String opcion = sc.nextLine();

            switch (opcion) {
                case "1" -> continuarComprando = procesarCompra(sc);
                case "2" -> {
                    System.out.println("Gracias por visitar el Teatro Moro!");
                    continuarComprando = false;
                }
                default -> System.out.println(" Opcion no valida. Intente nuevamente.");
            }
        }

        sc.close();
    }

    public static boolean procesarCompra(Scanner sc) {
        System.out.println("\n--- COMPRA DE ENTRADAS ---");

        mostrarPlanoTeatro();

        String zonaSeleccionada = "";
        boolean zonaValida = false;

        while (!zonaValida) {
            System.out.print("Seleccione una zona (A, B, C, D): ");
            zonaSeleccionada = sc.nextLine().toUpperCase();
            zonaValida = Arrays.asList(ZONAS).contains(zonaSeleccionada);
            if (!zonaValida) System.out.println(" Zona invalida. Intente nuevamente.");
        }

        int asientosPorZona = TOTAL_ASIENTOS / ZONAS.length;
        int cantidadEntradas = 0;

        while (true) {
            try {
                System.out.print("Cuantas entradas desea comprar? ");
                cantidadEntradas = Integer.parseInt(sc.nextLine());

                int disponibles = asientosPorZona - asientosOcupados.get(zonaSeleccionada).size();
                if (cantidadEntradas <= 0 || cantidadEntradas > disponibles) {
                    System.out.println(" Cantidad invalida o excede los asientos disponibles (" + disponibles + ").");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println(" Ingrese un numero valido.");
            }
        }

        List<String> asientosElegidos = new ArrayList<>();
        for (int i = 0; i < cantidadEntradas; i++) {
            while (true) {
                try {
                    System.out.print("Seleccione numero de asiento #" + (i + 1) + " en zona " + zonaSeleccionada + " (1 a" + asientosPorZona + "): ");
                    int numero = Integer.parseInt(sc.nextLine());
                    if (numero < 1 || numero > asientosPorZona) {
                        System.out.println(" Numero fuera de rango.");
                    } else if (asientosOcupados.get(zonaSeleccionada).contains(numero)) {
                        System.out.println(" Asiento ya ocupado. Elija otro.");
                    } else {
                        asientosOcupados.get(zonaSeleccionada).add(numero);
                        asientosElegidos.add(zonaSeleccionada + numero);
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println(" Entrada invalida.");
                }
            }
        }

        int edad = -1;
        while (edad < 0 || edad > 120) {
            try {
                System.out.print("Ingrese su edad: ");
                edad = Integer.parseInt(sc.nextLine());
                if (edad < 0 || edad > 120) {
                    System.out.println(" Edad no valida.");
                }
            } catch (NumberFormatException e) {
                System.out.println(" Ingrese un numero valido para la edad.");
            }
        }

        // Calcular descuento
        double descuento = (edad < 18) ? 0.10 : (edad > 64 ? 0.15 : 0);
        double totalBruto = PRECIO_BASE * cantidadEntradas;
        double totalDescuento = totalBruto * descuento;
        double totalFinal = totalBruto - totalDescuento;

        // Resumen
        System.out.println("\n--- RESUMEN DE COMPRA ---");
        System.out.println("Zona: " + zonaSeleccionada);
        System.out.println("Asientos comprados: " + asientosElegidos);
        System.out.println("Precio base por entrada: $" + PRECIO_BASE);
        System.out.println("Descuento aplicado: $" + totalDescuento + " (" + (int) (descuento * 100) + "%)");
        System.out.println("Total a pagar: $" + totalFinal);

        while (true) {
            System.out.print("\n Desea realizar otra compra? (S/N): ");
            String respuesta = sc.nextLine().toUpperCase();
            if (respuesta.equals("S")) return true;
            else if (respuesta.equals("N")) {
                System.out.println("Gracias por su compra!");
                return false;
            } else System.out.println(" Respuesta invalida.");
        }
    }

    public static void mostrarPlanoTeatro() {
        System.out.println("\n--- PLANO DEL TEATRO ---");
        int asientosPorZona = TOTAL_ASIENTOS / ZONAS.length;

        for (String zona : ZONAS) {
            System.out.print("Zona " + zona + ": ");
            for (int i = 1; i <= asientosPorZona; i++) {
                if (asientosOcupados.get(zona).contains(i)) {
                    System.out.print("[X]");
                } else {
                    System.out.print("[" + i + "]");
                }
            }
            System.out.println();
        }
    }
}
