package utils;

import modelo.Municipio;
import modelo.Paquete;
import servicio.QuindioExpress;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Mide tiempos con System.nanoTime().
 *
 * Parte 1: buscar un paquete por código de tres maneras (ArrayList lineal,
 *          ArrayList con búsqueda binaria y HashMap) con hasta 500.000 paquetes.
 * Parte 2: los métodos de QuindioExpress con 1.000, 5.000 y 10.000 paquetes.
 *          (No pasa de 10.000 porque los métodos recursivos hacen una llamada
 *           por paquete y con muchos más se llena la pila de llamadas.)
 */
public final class Benchmark {

    private static final int REPETICIONES = 100;

    private Benchmark() {
    }

    public static void ejecutar() {
        System.out.println("--- Parte 1: buscar por código ---");
        compararBusqueda();

        System.out.println();
        System.out.println("--- Parte 2: métodos de QuindioExpress ---");
        medirMetodos();
    }

    // =========================================================== PARTE 1

    private static void compararBusqueda() {
        int[] tamanos = {1_000, 10_000, 100_000, 500_000};

        System.out.printf("%-10s %-18s %-18s %-18s%n", "Paquetes", "Lineal (ns)", "Binaria (ns)", "HashMap (ns)");

        for (int n : tamanos) {
            // Los códigos llevan ceros a la izquierda (PQ0000001, PQ0000002...) para que la
            // lista quede ordenada por código, que es lo que necesita la búsqueda binaria.
            List<Paquete> lista = new ArrayList<>();
            Map<String, Paquete> mapa = new HashMap<>();
            for (int i = 0; i < n; i++) {
                String codigo = String.format("PQ%07d", i);
                Paquete p = new Paquete(codigo, Municipio.ARMENIA, 1.0, 5, 10);
                lista.add(p);
                mapa.put(codigo, p);
            }

            // Buscamos el ÚLTIMO código: es el peor caso para el recorrido lineal.
            String objetivo = lista.get(n - 1).getCodigo();

            long lineal = medir(() -> buscarLineal(lista, objetivo));
            long binaria = medir(() -> buscarBinaria(lista, objetivo));
            long hash = medir(() -> mapa.get(objetivo));

            System.out.printf("%-10d %-18s %-18s %-18s%n", n,
                    String.format("%,d", lineal),
                    String.format("%,d", binaria),
                    String.format("%,d", hash));
        }
        System.out.println("(tiempo promedio de una búsqueda, en nanosegundos)");
    }

    private static Paquete buscarLineal(List<Paquete> lista, String codigo) {
        for (Paquete p : lista) {
            if (p.getCodigo().equals(codigo)) {
                return p;
            }
        }
        return null;
    }

    // Precondición: la lista está ordenada por código.
    private static Paquete buscarBinaria(List<Paquete> ordenados, String codigo) {
        int izq = 0;
        int der = ordenados.size() - 1;

        while (izq <= der) {
            int medio = izq + (der - izq) / 2;
            Paquete actual = ordenados.get(medio);
            int cmp = codigo.compareTo(actual.getCodigo());

            if (cmp == 0) {
                return actual;
            } else if (cmp < 0) {
                der = medio - 1;
            } else {
                izq = medio + 1;
            }
        }
        return null;
    }

    // =========================================================== PARTE 2

    private static void medirMetodos() {
        int[] tamanos = {1_000, 5_000, 10_000};

        for (int n : tamanos) {
            System.out.println();
            System.out.println("n = " + n + " paquetes");
            List<Paquete> paquetes = crearPaquetes(n);

            // Registro: se mide el total y se divide entre n
            QuindioExpress s = new QuindioExpress("Benchmark");
            long inicio = System.nanoTime();
            for (Paquete p : paquetes) {
                s.registrarPaquete(p);
            }
            mostrar("registrarPaquete (promedio)", (System.nanoTime() - inicio) / n);

            String codigo = paquetes.get(0).getCodigo();
            List<Paquete> ordenados = s.ordenarCodigosPaquete();

            // Métodos que solo consultan: se repiten REPETICIONES veces
            mostrar("consultarPaquete (HashMap)", medir(() -> s.consultarPaquete(codigo)));
            mostrar("ordenarCodigosPaquete", medir(() -> s.ordenarCodigosPaquete()));
            mostrar("organizarPaquetesPrioridadDescendente", medir(() -> s.organizarPaquetesPrioridadDescendente()));
            mostrar("organizarPaquetesPesoDescendente", medir(() -> s.organizarPaquetesPesoDescendente()));
            mostrar("organizarPaquetesTiempoEstimado", medir(() -> s.organizarPaquetesTiempoEstimado()));
            mostrar("determinarTotalPesoPaquetes (recursivo)", medir(() -> s.determinarTotalPesoPaquetes(Municipio.ARMENIA)));
            mostrar("determinarNumeroPaquetePrioridadDada (recursivo)", medir(() -> s.determinarNumeroPaquetePrioridadDada(4)));
            mostrar("determinarPesoMayorPaquete (divide y vencerás)", medir(() -> s.determinarPesoMayorPaquete()));
            mostrar("buscarPaqueteCodigo (binaria)", medir(() -> s.buscarPaqueteCodigo(ordenados, codigo)));

            // Métodos que sacan paquetes: se vacía todo y se divide entre n
            QuindioExpress porPrioridad = nuevoSistema(paquetes);
            inicio = System.nanoTime();
            for (int i = 0; i < n; i++) {
                porPrioridad.atenderPaquetePrioridad();
            }
            mostrar("atenderPaquetePrioridad (promedio)", (System.nanoTime() - inicio) / n);

            QuindioExpress porLlegada = nuevoSistema(paquetes);
            inicio = System.nanoTime();
            for (int i = 0; i < n; i++) {
                porLlegada.atenderPaquetePorLlegada();
            }
            mostrar("atenderPaquetePorLlegada (promedio)", (System.nanoTime() - inicio) / n);
        }
    }

    private static QuindioExpress nuevoSistema(List<Paquete> paquetes) {
        QuindioExpress s = new QuindioExpress("Benchmark");
        for (Paquete p : paquetes) {
            s.registrarPaquete(p);
        }
        return s;
    }

    /** Crea n paquetes con códigos únicos y en desorden, y datos variados. */
    private static List<Paquete> crearPaquetes(int n) {
        List<Integer> numeros = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            numeros.add(i);
        }
        Collections.shuffle(numeros, new Random(1));   // misma mezcla en cada ejecución

        Municipio[] municipios = Municipio.values();
        List<Paquete> paquetes = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String codigo = String.format("PQ%07d", numeros.get(i));
            double peso = 1 + (i % 100) / 2.0;
            int prioridad = 1 + i % 5;
            int tiempo = 1 + i % 90;
            paquetes.add(new Paquete(codigo, municipios[i % municipios.length], peso, prioridad, tiempo));
        }
        return paquetes;
    }

    // =========================================================== AUXILIARES

    /** Ejecuta la operación varias veces y devuelve el promedio en nanosegundos. */
    private static long medir(Runnable operacion) {
        operacion.run(); // una ejecución previa para que Java "caliente" y no falsee la medición

        long inicio = System.nanoTime();
        for (int i = 0; i < REPETICIONES; i++) {
            operacion.run();
        }
        return (System.nanoTime() - inicio) / REPETICIONES;
    }

    private static void mostrar(String etiqueta, long nanos) {
        System.out.printf("  %-50s %,15d ns%n", etiqueta, nanos);
    }
}
