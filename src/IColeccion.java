public interface IColeccion <T> {
    void agregar(T elemento);
    boolean eliminar(T elemento);
    int tamano();
    boolean estaVacia();
    T obtener(int posicion);

}
