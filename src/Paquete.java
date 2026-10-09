import java.util.Objects;

public class Paquete implements Comparable<Paquete>{

    private String codigo;
    private Municipio destino;
    private double peso;
    private int prioridad;
    private int tiempoEstimado;

    public Paquete(String codigo,Municipio destino, double peso, int prioridad,
                   int tiempoEstimado){
        this.codigo = codigo;
        this.destino = destino;
        this.peso = peso;
        this.prioridad = prioridad;
        this.tiempoEstimado = tiempoEstimado;

    }

    public void setPeso(double peso) {
        if(peso < 0) throw new ArithmeticException("El peso debe ser mayor que cero");
        this.peso = peso;
    }

    public void setPrioridad(int prioridad) {
        if(prioridad < 1 || prioridad > 5 ) throw new RuntimeException("La prioridad debe estar entre ese rango");
        this.prioridad = prioridad;
    }

    public void setTiempoEstimado(int tiempoEstimado) {
        if(tiempoEstimado < 0) throw new RuntimeException("El tiempo debe ser mayor que 0");
        this.tiempoEstimado = tiempoEstimado;
    }

    public String getCodigo() {
        return codigo;
    }

    public Municipio getDestino() {
        return destino;
    }

    public double getPeso() {
        return peso;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public int getTiempoEstimado() {
        return tiempoEstimado;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Paquete paquete = (Paquete) o;
        return Objects.equals(getCodigo(), paquete.getCodigo());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getCodigo());
    }

    @Override
    public int compareTo(Paquete o) {
        return this.codigo.compareTo(o.getCodigo());
    }
}
