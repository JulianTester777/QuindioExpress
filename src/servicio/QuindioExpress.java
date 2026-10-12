package servicio;

import modelo.Municipio;
import modelo.Paquete;
import modelo.Repartidor;
import utils.CriteriosPaquete;

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

    //Necesidad 2 y 7: Busqueda rapida por codigo y la asociacion codigo->modelo.Paquete, se necesita un mapa para busqueda rapida
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

    //Asignaciones de repartidores
    private final Map<String, Repartidor> asignaciones = new HashMap<>();

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

    //Registrar repartidor
    public void registrarRepartidor(Repartidor r){
        if (r == null) {
            throw new IllegalArgumentException("El repartidor no puede ser nulo");
        }
        if (repartidores.containsKey(r.getId())) {
            throw new IllegalArgumentException("El repartidor con id " + r.getId() + " ya existe");
        }
        repartidores.put(r.getId(), r);
    }

    //Municipios con 1 paquete
    public Set<Municipio> determinarMunicipiosCon1PaqueteRegistrado(){
        return municipios;
    }

    //Atender paquete por prioridad
    public Paquete atenderPaquetePrioridad(){
        Paquete p = prioridadPaquetes.poll();
        if(p == null){
            throw new NoSuchElementException("No hay paquetes por atender");
        }
        ordenRegistro.remove(p);
        return p;
    }

    //Atender paquete por Llegada
    public Paquete atenderPaquetePorLlegada(){
        if(ordenRegistro.isEmpty()){
            throw new NoSuchElementException("No hay paquetes pendientes");
        }
        Paquete p = ordenRegistro.remove(0);
        prioridadPaquetes.remove(p);
        return p;
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
    //Organizar paquetes por tiempo estimado
    public List<Paquete> organizarPaquetesTiempoEstimado(){
        List<Paquete> organizados = new ArrayList<>(ordenRegistro);
        organizados.sort(CriteriosPaquete.POR_TIEMPO_ASC);
        return organizados;
    }
    //Consultar paquete por su codigo
    public Paquete consultarPaquete(String id){
        return porCodigo.get(id); //Null si no existe
    }

    //Consultar repartidor por su id
    public Repartidor consultarRepartidor(String idRepartidor){
        return repartidores.get(idRepartidor); //Null si no existe
    }

    //Municipios ordenados alfabeticamente
    public Set<Municipio> obtenerMunicipiosOrdenados(){
        return municipiosOrdenados;  // Mano no se bien si hice el metodo bien osea no lo entendi muy bien
    }

    public void consultarPaquetesMunicipio(Municipio municipioConsultar){
        List<Paquete> listPaquetes = porMunicipio.get(municipioConsultar);
        if (listPaquetes == null){
            throw new NoSuchElementException("No hay paquetes para el municipio " + municipioConsultar);
        }
        System.out.println(municipioConsultar + " -> " + listPaquetes);
    }


    public Paquete verSiguientePaqueteDespacho(){
        Paquete siguiente = prioridadPaquetes.peek();   // null si está vacía
        if (siguiente == null) {
            throw new NoSuchElementException("No hay paquetes pendientes");
        }
        return siguiente;
    }

    public void asignarPaqueteRepartidor(String codigoPaquete, String idRepartidor){
        Paquete p = porCodigo.get(codigoPaquete);
        if (p == null) {
            throw new NoSuchElementException("No existe el paquete " + codigoPaquete);
        }

        if (asignaciones.containsKey(codigoPaquete)) {
            throw new IllegalStateException("El paquete " + codigoPaquete + " ya fue asignado");
        }

        Repartidor r = repartidores.get(idRepartidor);
        if (r == null) {
            throw new NoSuchElementException("No existe el repartidor " + idRepartidor);
        }

        // 1. Validar primero (disponible y zona == destino)
        if (!r.puedeRecibirPaquete(p)) {
            throw new IllegalStateException(
                    "El repartidor " + idRepartidor + " no puede recibir el paquete " + codigoPaquete);
        }

        // 2. Solo después, cambiar el estado
        r.setDisponible(false);
        asignaciones.put(codigoPaquete,r);
    }

    //Cambia la disponibilidad del repartidor
    public void cambiarDisponibilidadRepartidor(String idRepartidor, boolean disponibilidad){
        Repartidor r = repartidores.get(idRepartidor);
        if(r == null){
            throw new NoSuchElementException("No se encuentra el repartidor");
        }
        r.setDisponible(disponibilidad);
    }

    public void RegistrarEntrega(String codigo){
        // 1. Buscar el paquete (si no existe, no hay nada que entregar)
        Paquete p = porCodigo.get(codigo);
        if (p == null) {
            throw new NoSuchElementException("No existe el paquete " + codigo);
        }

        // 2. Quitarlo de las estructuras de pendientes
        porCodigo.remove(codigo);
        ordenRegistro.remove(p);
        prioridadPaquetes.remove(p);

        // 3. Quitarlo de su municipio
        Municipio destino = p.getDestino();
        List<Paquete> delMunicipio = porMunicipio.get(destino);
        if (delMunicipio != null) {
            delMunicipio.remove(p);
            if (delMunicipio.isEmpty()) {          // al municipio no le quedan pendientes
                porMunicipio.remove(destino);
                municipios.remove(destino);
                municipiosOrdenados.remove(destino);
            }
        }

        Repartidor r = asignaciones.remove(codigo);
        if(r != null){
            r.setDisponible(true);
        }

        // 4. Guardarlo en el historial, al final (orden de entrega)
        historialPaquete.registrarHistorial(p);

    }

        // ---- Peso total por municipio (recursivo)
        public double determinarTotalPesoPaquetes(Municipio m) {
            return pesoMunicipio(ordenRegistro, m, 0);
        }

        private double pesoMunicipio(List<Paquete> lista, Municipio m, int i) {
            if (i == lista.size()) return 0.0;                              // caso base
            double actual = lista.get(i).getDestino() == m ? lista.get(i).getPeso() : 0.0;
            return actual + pesoMunicipio(lista, m, i + 1);                 // caso recursivo
        }


        // ---- Cantidad con prioridad mínima (recursivo)
        public int determinarNumeroPaquetePrioridadDada(int prioridadMinima) {
            return contarPrioridad(ordenRegistro, prioridadMinima, 0);
        }

        private int contarPrioridad(List<Paquete> lista, int minima, int i) {
            if (i == lista.size()) return 0;
            int suma = lista.get(i).getPrioridad() >= minima ? 1 : 0;
            return suma + contarPrioridad(lista, minima, i + 1);
        }


    //BusquedaBinaria
        public Paquete buscarPaqueteCodigo (List<Paquete> ordenados, String idCodigo){
            int izq = 0;
            int der = ordenados.size()-1;

            while(izq <= der){
                int medio = izq + (der-izq)/2;
                Paquete actual = ordenados.get(medio);
                int cmp = idCodigo.compareTo(actual.getCodigo());
                if (cmp == 0) {
                    return actual;
                }else if(cmp<0){
                    der = medio-1;
                }else {
                    izq = medio+1;
                }
            }
            throw new NoSuchElementException("No hay un paquete del codigo " + idCodigo);
        }

        // ---- Mayor peso (divide y vencerás)
        public Paquete determinarPesoMayorPaquete() {
            if (ordenRegistro.isEmpty()) {
                throw new NoSuchElementException("No hay paquetes registrados");
            }
            return mayorPeso(ordenRegistro, 0, ordenRegistro.size() - 1);
        }

        private Paquete mayorPeso(List<Paquete> lista, int izq, int der) {
            if (izq == der) return lista.get(izq);                          // caso base
            int mitad = izq + (der - izq) / 2;                              // división
            Paquete mayorIzq = mayorPeso(lista, izq, mitad);                // subproblema izquierdo
            Paquete mayorDer = mayorPeso(lista, mitad + 1, der);            // subproblema derecho
            return mayorIzq.getPeso() >= mayorDer.getPeso() ? mayorIzq : mayorDer;   // combinación
        }

    public HistorialPaquete getHistorialPaquete() {
        return historialPaquete;
    }
}

