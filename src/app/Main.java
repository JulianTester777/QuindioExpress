package app;

import modelo.Municipio;
import modelo.Paquete;
import modelo.Repartidor;
import servicio.QuindioExpress;
import utils.Benchmark;
import utils.CargarDatos;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        QuindioExpress sistema = new QuindioExpress("QuindioExpress");

        // ------------------------------------------------------------------ 1
        titulo("1. CARGA DE DATOS");
        long inicio = System.nanoTime();
        CargarDatos.cargarTodo(sistema);
        mostrarTiempo("Carga inicial (30 paquetes, 8 repartidores)", System.nanoTime() - inicio);

        // ------------------------------------------------------------------ 2
        titulo("2. VALIDACIONES");
        intentar("Registrar un código repetido (PQ731)",
                () -> sistema.registrarPaquete(new Paquete("PQ731", Municipio.ARMENIA, 1.0, 5, 10)));
        intentar("Crear un paquete con peso negativo",
                () -> new Paquete("PQ999", Municipio.ARMENIA, -2.0, 5, 10));
        intentar("Registrar un repartidor con id repetido (R01)",
                () -> sistema.registrarRepartidor(new Repartidor("R01", "Otro", Municipio.SALENTO, true)));

        // ------------------------------------------------------------------ 3
        titulo("3. CONSULTAS POR CÓDIGO (HashMap)");
        System.out.println("PQ731 -> " + sistema.consultarPaquete("PQ731"));
        System.out.println("PQ000 -> " + sistema.consultarPaquete("PQ000") + "   (null = no existe)");
        System.out.println("R03   -> " + sistema.consultarRepartidor("R03"));
        promedio("consultarPaquete", 100_000, () -> sistema.consultarPaquete("PQ731"));

        // ------------------------------------------------------------------ 4
        titulo("4. MUNICIPIOS");
        System.out.println("Sin repetir : " + sistema.determinarMunicipiosCon1PaqueteRegistrado());
        System.out.println("Ordenados   : " + sistema.obtenerMunicipiosOrdenados());
        sistema.consultarPaquetesMunicipio(Municipio.ARMENIA);
        sistema.consultarPaquetesMunicipio(Municipio.PIJAO);

        // ------------------------------------------------------------------ 5
        titulo("5. ORDENAMIENTOS (copias; no alteran el orden de llegada)");
        mostrarPrimeros("Código ascendente (orden natural)", sistema.ordenarCodigosPaquete(), 5);
        mostrarPrimeros("Prioridad descendente", sistema.organizarPaquetesPrioridadDescendente(), 5);
        mostrarPrimeros("Peso descendente", sistema.organizarPaquetesPesoDescendente(), 5);
        mostrarPrimeros("Tiempo estimado ascendente", sistema.organizarPaquetesTiempoEstimado(), 5);
        promedio("ordenarCodigosPaquete (n=30)", 10_000, () -> sistema.ordenarCodigosPaquete());

        // ------------------------------------------------------------------ 6
        titulo("6. RECURSIVIDAD, DIVIDE Y VENCERÁS Y BÚSQUEDA BINARIA");
        System.out.printf("Peso total ARMENIA            : %.1f kg   (esperado 55.6)%n",
                sistema.determinarTotalPesoPaquetes(Municipio.ARMENIA));
        System.out.println("Paquetes con prioridad >= 4   : "
                + sistema.determinarNumeroPaquetePrioridadDada(4) + "   (esperado 14)");
        System.out.println("Paquete de mayor peso (DyV)   : "
                + sistema.determinarPesoMayorPaquete() + "   (esperado PQ829, 18.7 kg)");

        List<Paquete> ordenados = sistema.ordenarCodigosPaquete(); // precondición de la búsqueda binaria
        System.out.println("Búsqueda binaria PQ731        : " + sistema.buscarPaqueteCodigo(ordenados, "PQ731"));
        intentar("Búsqueda binaria de PQ000 (no existe)",
                () -> sistema.buscarPaqueteCodigo(ordenados, "PQ000"));

        promedio("determinarTotalPesoPaquetes (recursivo)", 10_000,
                () -> sistema.determinarTotalPesoPaquetes(Municipio.ARMENIA));
        promedio("determinarNumeroPaquetePrioridadDada (recursivo)", 10_000,
                () -> sistema.determinarNumeroPaquetePrioridadDada(4));
        promedio("determinarPesoMayorPaquete (DyV)", 10_000,
                () -> sistema.determinarPesoMayorPaquete());
        promedio("buscarPaqueteCodigo (binaria)", 100_000,
                () -> sistema.buscarPaqueteCodigo(ordenados, "PQ731"));

        // ------------------------------------------------------------------ 7
        titulo("7. ATENCIÓN DE PAQUETES");
        System.out.println("Siguiente a despachar        : " + sistema.verSiguientePaqueteDespacho());
        System.out.println("Consultado otra vez (no sale): " + sistema.verSiguientePaqueteDespacho());

        System.out.println("\nPor prioridad (esperado PQ829, PQ655, PQ162, PQ450, PQ105, PQ863):");
        for (int i = 1; i <= 6; i++) {
            System.out.println("  " + i + ". " + sistema.atenderPaquetePrioridad().getCodigo());
        }

        System.out.println("\nPor orden de llegada (esperado PQ731, PQ942, PQ318; PQ105 ya salió):");
        for (int i = 1; i <= 3; i++) {
            System.out.println("  " + i + ". " + sistema.atenderPaquetePorLlegada().getCodigo());
        }

        // ------------------------------------------------------------------ 8
        titulo("8. ASIGNACIÓN, ENTREGA E HISTORIAL");
        sistema.asignarPaqueteRepartidor("PQ105", "R02");   // SALENTO -> R02 (SALENTO)
        sistema.asignarPaqueteRepartidor("PQ942", "R01");   // ARMENIA -> R01 (ARMENIA)
        System.out.println("PQ105 -> R02 y PQ942 -> R01 asignados");
        System.out.println("R01 disponible: " + sistema.consultarRepartidor("R01").isDisponible()
                + "   (esperado false)");

        intentar("PQ731 a R03 (zona distinta)",
                () -> sistema.asignarPaqueteRepartidor("PQ731", "R03"));
        intentar("PQ731 a R08 (no disponible)",
                () -> sistema.asignarPaqueteRepartidor("PQ731", "R08"));
        intentar("PQ105 a R03 (ya estaba asignado)",
                () -> sistema.asignarPaqueteRepartidor("PQ105", "R03"));

        inicio = System.nanoTime();
        sistema.RegistrarEntrega("PQ105");
        mostrarTiempo("registrarEntrega(PQ105)", System.nanoTime() - inicio);
        sistema.RegistrarEntrega("PQ942");
        System.out.println("R01 disponible tras entregar: " + sistema.consultarRepartidor("R01").isDisponible()
                + "   (esperado true)");

        sistema.asignarPaqueteRepartidor("PQ731", "R01");   // R01 ya quedó libre
        sistema.RegistrarEntrega("PQ731");

        System.out.println("\nHistorial (esperado PQ105 -> PQ942 -> PQ731):");
        sistema.getHistorialPaquete().mostrarHistorial();
        System.out.println("Tamaño del historial: " + sistema.getHistorialPaquete().obtenerTamano());
        System.out.println("PQ105 sigue pendiente: " + sistema.consultarPaquete("PQ105") + "   (esperado null)");

        // ------------------------------------------------------------------ 9
        titulo("9. TIEMPOS CON MUCHOS DATOS (Partes A.2 y C)");
        Benchmark.ejecutar();
    }

    // ===================================================================== utilidades

    private static void titulo(String texto) {
        System.out.println();
        System.out.println("=== " + texto + " ===");
    }

    private static void mostrarTiempo(String etiqueta, long nanos) {
        System.out.printf("  [tiempo] %-45s %,d ns (%.3f ms)%n", etiqueta, nanos, nanos / 1_000_000.0);
    }

    /** Ejecuta la operación varias veces y muestra el tiempo promedio por llamada. */
    private static void promedio(String etiqueta, int repeticiones, Runnable operacion) {
        for (int i = 0; i < repeticiones / 10; i++) {   // calentamiento
            operacion.run();
        }
        long inicio = System.nanoTime();
        for (int i = 0; i < repeticiones; i++) {
            operacion.run();
        }
        long promedio = (System.nanoTime() - inicio) / repeticiones;
        System.out.printf("  [tiempo] %-45s %,d ns por llamada (%,d repeticiones)%n",
                etiqueta, promedio, repeticiones);
    }

    /** Ejecuta algo que debe fallar y muestra el mensaje de la excepción. */
    private static void intentar(String descripcion, Runnable operacion) {
        try {
            operacion.run();
            System.out.println("  " + descripcion + " -> NO lanzó excepción (revisar)");
        } catch (RuntimeException e) {
            System.out.println("  " + descripcion + " -> " + e.getClass().getSimpleName()
                    + ": " + e.getMessage());
        }
    }

    private static void mostrarPrimeros(String titulo, List<Paquete> lista, int cantidad) {
        System.out.println(titulo + ":");
        for (int i = 0; i < cantidad && i < lista.size(); i++) {
            Paquete p = lista.get(i);
            System.out.printf("  %d. %s | %s | %.1f kg | prio %d | %d min%n",
                    i + 1, p.getCodigo(), p.getDestino(), p.getPeso(),
                    p.getPrioridad(), p.getTiempoEstimado());
        }
    }
}
 