import java.util.Iterator;

public class ListaEnlazadaPropia<T> implements IColeccion<T>, Iterable<T>{

    private static class Nodo<E>{
        private E dato;
        private Nodo<E> siguiente;

        private Nodo(E dato){
            this.dato = dato;
            siguiente = null;
        }
    }

    private Nodo<T> cabeza;
    private int tamano;

    public ListaEnlazadaPropia(){
        cabeza = null;
        tamano = 0;
    }

    @Override
    public void agregar(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        if (cabeza == null) {
            cabeza = nuevo;
        } else {
            Nodo<T> actual = cabeza;
            while (actual.siguiente != null) {
                actual = actual.siguiente;
            }
            actual.siguiente = nuevo;
        }
        tamano++;
    }

    @Override
    public Iterator<T> iterator() {
        return null;
    }


    @Override
    public void eliminar(T elemento) {

    }

    @Override
    public boolean contiene(T elemento) {
        return false;
    }

    @Override
    public int tamaño() {
        return 0;
    }

    @Override
    public boolean estaVacia() {
        return false;
    }




}
