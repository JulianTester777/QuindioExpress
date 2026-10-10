import java.util.*;

public class QuindioExpress {
    private String nombre;

    public QuindioExpress(String nombre){
        this.nombre = "Quindio Express"; //Valor por defecto
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    //Necesidad 1: Orden exacto de registro de paquetes, se necesita una lista para mantener el orden de llegada
    private final List<Paquete> ordenRegistro = new ArrayList<>();

    //Necesidad 2 y 7: Busqueda rapida por codigo y la asociacion codigo->Paquete, se necesita un mapa para busqueda rapida
    private final Map<String, Paquete> porCodigo = new HashMap<>();

    //Necesidad 3: Municipios sin duplicados
    private final Set<Municipio> municipios = new HashSet<>();

    //Necesidad 4: Municipios ordenados alfabeticamente, se necesita un conjunto ordenado
    private final Set<Municipio> municipiosOrdenados = new TreeSet<>(Comparator.comparing(Municipio::name));

    //Necesidad 5: Despacho por prioridad de paquetes
    private final Queue<Paquete> prioridadPaquetes = new PriorityQueue<>(CriteriosPaquete.DESPACHO);

    //Necesidad 6: Historial de paquetes entregados, en orden de entrega (usa la lista propia)
    private final HistorialPaquete historialPaquete = new HistorialPaquete();

    //Agrupación de paquetes por destino
    private final Map<Municipio, List<Paquete>> porMunicipio = new HashMap<>();

    //Repartidores
    private final Map<String, Repartidor> repartidores = new HashMap<>();


    //Registro de paquetes
    public void registrarPaquete(Paquete p) {
        if (p == null) {
            throw new IllegalArgumentException("El paquete no puede ser nulo");
        }
        if (porCodigo.containsKey(p.getCodigo())) {
            throw new IllegalArgumentException("El paquete con codigo " + p.getCodigo() + " ya existe");
        }

        porCodigo.put(p.getCodigo(), p);
        ordenRegistro.add(p);                       // conserva orden de llegada
        municipios.add(p.getDestino());             // sin duplicados
        municipiosOrdenados.add(p.getDestino());    // orden alfabético
        porMunicipio.computeIfAbsent(p.getDestino(), k -> new ArrayList<>()).add(p);
        prioridadPaquetes.offer(p);                 // despacho prioritario
    }

    //Municipios con 1 paquete
    public Set<Municipio> determinarMunicipiosCon1PaqueteRegistrado(){
        if()
    }
    //Atender paquete por prioridad
    public Paquete atenderPaquetePrioridad(){
        return prioridadPaquetes.poll();
    }
    //Municipios con paquetes asignados
    public Set<Municipio> consultarMunicipiosConPaqueteAsignado(){
        return Set.of(Municipio.values());
    }
    //Ordenar paquetes por codigo
    public List<Paquete> ordenarCodigosPaquete() {
        List<Paquete> ordenados = new ArrayList<>(ordenRegistro);
        Collections.sort(ordenados);
        return ordenados;
    }
    //Organizar paquetes por prioridad descendente
    public List<Paquete> organizarPaquetesPrioridadDescendente(){
        List<Paquete> organizados = new ArrayList<>(ordenRegistro);
        organizados.sort(CriteriosPaquete.POR_PRIORIDAD_DESC);
        return organizados;
    }
    //Organizar paquetes por peso descendente
    public List<Paquete> organizarPaquetesPesoDescendente(){
        List<Paquete> organizados = new ArrayList<>(ordenRegistro);
        organizados.sort(CriteriosPaquete.POR_PESO_DESC);
        return organizados;
    }

    public void organizarPaquetesTiempoEstimado(){

    }

    public void consultarPaquete(String id){

    }

    public void registrarRepartidor(){

    }

    public void consultarRepartidor(){

    }

    public void obtenerMunicipiosOrdenados(){

    }

    public void consultarPaquetesMunicipio(Municipio municipioConsultar){

    }

    public void atenderPaquetePorLlegada(){

    }

    public void verSiguientePaqueteDespacho(){

    }

    public void asignarPaqueteRepartidor(String idRepartidor){

    }

    public void cambiarDisponibilidadRepartidor(String idRepartidor){

    }

    public void RegistrarEntrega(){

    }

    //Metodo recursivo
    public void determinarTotalPesoPaquetes(Municipio m){

    }

    //Metodo recursivo
    public void determinarNumeroPaquetePrioridadDada(){

    }

    //Metodo usando Divide y Venceras
    public void organizarPaquetesCodigo(){

    }

    //BusquedaBinaria
    public void buscarPaqueteCodigo(String idCodigo){

    }

    //Metodo usando Divide y Venceras
    public void determinarPesoMayorPaquete(){

    }









}
