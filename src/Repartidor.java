import java.util.NoSuchElementException;

public class Repartidor {

    private String id;
    private String nombre;
    private Municipio zona;
    private boolean disponible;

    public Repartidor(String id,String nombre,Municipio zona,
                      boolean disponible){
        this.id = id;
        this.nombre = nombre;
        this.zona = zona;
        this.disponible = true;

    }
    //Se utiliza cuando se vaya a usar en la clase QuindioExpress al asignar
    // un paquete a un repartidor mirar si esta disponible o no y la zona coincide
    public boolean puedeRecibirPaquete(Paquete p){
        if (p == null) throw new IllegalArgumentException("El paquete no existe");
        return disponible && zona == p.getDestino();
    }

    public String getNombre() {
        return nombre;
    }


    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Municipio getZona() {
        return zona;
    }

    public void setZona(Municipio zona) {
        this.zona = zona;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    @Override
    public String toString() {
        return "Repartidor{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", zona=" + zona +
                ", disponible=" + disponible +
                '}';
    }
}
