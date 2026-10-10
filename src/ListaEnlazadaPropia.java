import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class ListaEnlazadaPropia<T> implements IColeccion<T>, Iterable<T>{
    //clase privada Nodo
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

    // ------------------Fin de la clase privada Nodo ------------------------------

    //-------------------Clase IteradorLista ---------------------------------
    private class IteradorLista implements Iterator<T>{
        private Nodo<T> siguiente = cabeza;
        private Nodo<T> ultimo = null; //el que devolverá next
        private Nodo<T> anterior = null; // el ultimo que devolvio next
        private boolean puedeEliminar = false; // el previo a ultimo

        @Override
        public boolean hasNext() {
            return siguiente != null;
        }

        @Override
        public T next() {
            if(!hasNext()) throw new NoSuchElementException();

            if(ultimo != null) anterior = ultimo;
            ultimo = siguiente;
            siguiente = siguiente.siguiente;
            puedeEliminar = true;

            return ultimo.dato;

        }
        @Override
        public void remove(){
            if(!puedeEliminar) throw new IllegalStateException();

            if(anterior == null){
                cabeza = siguiente;
            }else{
                anterior.siguiente = siguiente;
            }

            ultimo = null;
            puedeEliminar = false;
            tamano--;

        }

    }
    //---------------------------Fin Clase IteradorLista ---------------------------

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
        return new IteradorLista();
    }

    @Override
    public void eliminar(T elemento) {
        IteradorLista it = new IteradorLista();
        while(it.hasNext()) {
            if (contiene(elemento)) {
                it.remove(); //borra el que next aca de devolver
                return;     //solo la primera coincidencia
            }
        }
    }

    @Override
    public boolean contiene(T elemento) {
        IteradorLista it = new IteradorLista();
        while(it.hasNext()){
            if (Objects.equals(it.next(), elemento)){
                return true;
            }
        }
        return false;
    }

    @Override
    public int tamano() {
        return 0;
    }

    @Override
    public boolean estaVacia() {
        return tamano == 0;
    }




}
