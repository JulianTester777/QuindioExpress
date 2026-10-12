package servicio;

import estructuras.ListaEnlazadaPropia;
import modelo.Paquete;

import java.util.Iterator;

public class HistorialPaquete implements Iterable<Paquete> {

    private ListaEnlazadaPropia<Paquete> listaEnlazadaPropia;

    public HistorialPaquete(){

        this.listaEnlazadaPropia = new ListaEnlazadaPropia<Paquete>();
    }

    public void registrarHistorial(Paquete p) {
        if (p == null) {
            throw new IllegalArgumentException("El paquete no puede ser nulo");
        }
        listaEnlazadaPropia.agregar(p);
    }

    public int obtenerTamano() {
        return listaEnlazadaPropia.tamano();
    }

    public boolean estaVacio() {
        return listaEnlazadaPropia.estaVacia();
    }

    public Paquete obtener(int posicion) {
        return listaEnlazadaPropia.obtener(posicion);
    }

    public Iterator<Paquete> iterator() {
        return listaEnlazadaPropia.iterator();
    }

    public void mostrarHistorial() {
        if (estaVacio()) {
            System.out.println("El historial está vacío");
            return;
        }
        for (Paquete p : this) {
            System.out.println(p);
        }
    }



}
