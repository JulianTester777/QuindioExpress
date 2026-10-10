public interface IColeccion <T> {
    void agregar(T elemento);
    void eliminar(T elemento);
    boolean contiene(T elemento);
    int tamaño();
    boolean estaVacia();
}
