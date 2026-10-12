package utils;

import modelo.Municipio;
import modelo.Paquete;
import modelo.Repartidor;
import servicio.QuindioExpress;

/**
 * Datos iniciales de la jornada (definidos directamente en Java).
 * Cumple el mínimo del enunciado: 30 paquetes, 7 destinos, 8 repartidores,
 * prioridades y pesos variados, códigos no consecutivos y varios paquetes
 * al mismo municipio.
 */
public final class CargarDatos {

    private CargarDatos() {
    }

    public static void cargarTodo(QuindioExpress s) {
        cargarPaquetes(s);
        cargarRepartidores(s);
    }

    public static void cargarPaquetes(QuindioExpress s) {
        // El orden de estas líneas es el orden de llegada.
        // Paquete(codigo, destino, peso, prioridad, tiempoEstimado)
        s.registrarPaquete(new Paquete("PQ731", Municipio.ARMENIA, 4.5, 5, 45));
        s.registrarPaquete(new Paquete("PQ105", Municipio.SALENTO, 3.0, 5, 25));
        s.registrarPaquete(new Paquete("PQ942", Municipio.ARMENIA, 7.2, 4, 20));
        s.registrarPaquete(new Paquete("PQ318", Municipio.CALARCA, 2.3, 3, 35));
        s.registrarPaquete(new Paquete("PQ567", Municipio.SALENTO, 12.0, 2, 50));
        s.registrarPaquete(new Paquete("PQ829", Municipio.ARMENIA, 18.7, 5, 15));
        s.registrarPaquete(new Paquete("PQ420", Municipio.CIRCASIA, 3.2, 3, 30));
        s.registrarPaquete(new Paquete("PQ214", Municipio.QUIMBAYA, 7.4, 1, 60));
        s.registrarPaquete(new Paquete("PQ421", Municipio.MONTENEGRO, 5.1, 4, 40));
        s.registrarPaquete(new Paquete("PQ842", Municipio.PIJAO, 9.8, 2, 55));
        s.registrarPaquete(new Paquete("PQ203", Municipio.ARMENIA, 1.5, 3, 22));
        s.registrarPaquete(new Paquete("PQ415", Municipio.CALARCA, 6.6, 5, 28));
        s.registrarPaquete(new Paquete("PQ730", Municipio.SALENTO, 2.8, 4, 33));
        s.registrarPaquete(new Paquete("PQ101", Municipio.CIRCASIA, 10.4, 1, 48));
        s.registrarPaquete(new Paquete("PQ655", Municipio.QUIMBAYA, 4.1, 5, 18));
        s.registrarPaquete(new Paquete("PQ377", Municipio.MONTENEGRO, 15.3, 3, 42));
        s.registrarPaquete(new Paquete("PQ908", Municipio.ARMENIA, 8.9, 2, 26));
        s.registrarPaquete(new Paquete("PQ512", Municipio.PIJAO, 0.9, 4, 65));
        s.registrarPaquete(new Paquete("PQ249", Municipio.CALARCA, 13.2, 1, 38));
        s.registrarPaquete(new Paquete("PQ863", Municipio.ARMENIA, 5.5, 5, 25)); // empata con PQ105 (prio 5, 25 min)
        s.registrarPaquete(new Paquete("PQ196", Municipio.SALENTO, 6.1, 3, 29));
        s.registrarPaquete(new Paquete("PQ774", Municipio.CIRCASIA, 2.2, 2, 52));
        s.registrarPaquete(new Paquete("PQ338", Municipio.QUIMBAYA, 11.7, 4, 31));
        s.registrarPaquete(new Paquete("PQ691", Municipio.MONTENEGRO, 3.6, 1, 44));
        s.registrarPaquete(new Paquete("PQ450", Municipio.PIJAO, 7.9, 5, 21));
        s.registrarPaquete(new Paquete("PQ927", Municipio.CALARCA, 4.8, 2, 36));
        s.registrarPaquete(new Paquete("PQ583", Municipio.ARMENIA, 9.3, 3, 24));
        s.registrarPaquete(new Paquete("PQ162", Municipio.SALENTO, 14.5, 5, 19));
        s.registrarPaquete(new Paquete("PQ806", Municipio.CIRCASIA, 1.1, 4, 41));
        s.registrarPaquete(new Paquete("PQ299", Municipio.QUIMBAYA, 5.9, 3, 27));
    }

    public static void cargarRepartidores(QuindioExpress s) {
        // Repartidor(id, nombre, zona)  -> disponible = true por defecto
        s.registrarRepartidor(new Repartidor("R01", "Carlos Gómez", Municipio.ARMENIA,true));
        s.registrarRepartidor(new Repartidor("R02", "Laura Ríos", Municipio.SALENTO,true));
        s.registrarRepartidor(new Repartidor("R03", "Andrés Salazar", Municipio.CALARCA,true));
        s.registrarRepartidor(new Repartidor("R04", "Marta Henao", Municipio.QUIMBAYA,true));
        s.registrarRepartidor(new Repartidor("R05", "Julián Osorio", Municipio.CIRCASIA,true));
        s.registrarRepartidor(new Repartidor("R06", "Paula Cardona", Municipio.MONTENEGRO,true));
        s.registrarRepartidor(new Repartidor("R07", "Sergio Londoño", Municipio.PIJAO,true));

        // Un segundo repartidor de Armenia que NO está disponible (para probar esa regla)
        Repartidor r08 = new Repartidor("R08", "Diana Ocampo", Municipio.ARMENIA,true);
        r08.setDisponible(false);
        s.registrarRepartidor(r08);
    }
}
