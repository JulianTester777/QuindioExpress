QUINDIO EXPRESS

Introducción

QuindioExpress una empresa que presta servicios de recepcion,clasificacion y distribución de paquetes hacia diferentes municipios del departamento del Quindio.Cada dia el centro de distribuccion recibe una cantidad sonsiderable de paquetes provenientes de distintos remitentes, los cuales ingresan al sistema en el mismo orden en que llegan, y pasan por diferentes procesos: 
-Registro
-Consulta
-Priorizacion
-Asignacion para despacho y registro de entrega

El proyecto demuestra el uso de las colecciones del Java Collections Framework, una lista enlazada propia, recursividad, búsqueda binaria y divide y venceras

Estructura del proyecto 
Quisimos organizarla asi porq tratamos de modularizar las clases y que no viera desorganizado

src/
├── app/
│   └── Main.java                 # Punto de entrada
├── estructuras/
│   ├── IColeccion.java           # Interfaz genérica de colección
│   └── ListaEnlazadaPropia.java  # Lista enlazada propia (genérica, Iterable)
├── modelo/
│   ├── Municipio.java            # Enum de municipios destino
│   ├── Paquete.java              # Entidad paquete (Comparable)
│   └── Repartidor.java           # Entidad repartidor
├── servicio/
│   ├── HistorialPaquete.java     # Historial de entregas (usa la lista propia)
│   └── QuindioExpress.java       # Lógica de negocio central
└── utils/
    ├── Benchmark.java            # Medición de tiempos (Parte A.2 y C)
    ├── CargarDatos.java          # Datos iniciales de la jornada
    └── CriteriosPaquete.java     # Comparadores de paquetes

Clases y Responsabilidades 

/modelo-Entidades
*Municipio— Enum con los 7 destinos: ARMENIA, SALENTO, CALARCA, QUIMBAYA, CIRCASIA, MONTENEGRO, PIJAO
*Paquete— Implementa Comparable<Paquete>, el orden natural es por código ascendente. Valida peso, prioridad (1–5) y tiempo estimado. equals() y hashCode() se basan en el código.
*Repartidor— Tiene id, nombre, zona y disponibilidad. El método puedeRecibirPaquete(p) verifica que esté disponible y que su zona coincida con el destino del paquete.

/estructuras-Coleccion propia
*IColeccion<T>— Interfaz genérica con agregar, eliminar, tamano, estaVacia y obtener
*ListaEnlazadaPropia<T>— Implementa IColeccion<T> e Iterable<T>. Incluye la clase interna Nodo<E> y el iterador IteradorLista con soporte para remove(). No usa ninguna colección de Java internamente.⁠

/servicio- Lógica
*HistorialPaquete — Envuelve la ListaEnlazadaPropia<Paquete> y expone registrarHistorial, obtenerTamano, estaVacio, obtener, iterator y mostrarHistorial.
*QuindioExpress— Clase central que mantiene todas las colecciones y las operaciones del sistema 

/utils- Soporte
*CriteriosPaquete — Comparadores reutilizables: por prioridad descendente, por peso descendente, por tiempo ascendente, y el combinado DESPACHO prioridad → tiempo → código
*CargarDatos— Carga 30 paquetes y 8 repartidores; los paquetes se registran en el orden de llegada
*Benchmark — Mide con System.nanoTime() la búsqueda por código (lineal, binaria, HashMap) y los métodos del sistema con 1.000, 5.000 y 10.000 paquetes


Identificación de necesidades y selección de estructuras

A partir del enunciado se identifican 7 necesidad primordiales, para cada una se selecciono una estructura apropiada que cumpliera con los requisitos establecidos y considerando la operacion dominante que debe soportar y su complejidad esperada

Necesidad 1- Mantener el orden exacto de registro de los paquetes 
Se eligio un ArrayList<Paquete>, cuya operacion principal es add(), con complejidad 0(1).S e descarto un LinkedList porq el acceso por posicion es mas costoso y aqui la operacion dominante es simplemente agg al final y recorrer a lo cual el ArrayList es suficiente y mas eficiente en memoria

Necesidad 2- Consultar rapidamente un paquete mediante su codigo 
Se eligio un HashMap<String,Paquete>, la operacion dominante get(codigo) con complejidad O(1) esta estructura nos evita recorrer innecesariamente todos los paquetes 

Necesidad 3- Mantener el conjunto de municipios destino evitando duplicados 
Se eligio un HashSet<Municipio>, la operacion dominante add() tiene complejidad 0(1) y garantiza automaticamente la unicidad de los elementos 

Necesidad 4- Mantener los municipios ordenados alfabeticamente y sin repetidos 
Se eligio un TreeSet<Municipio> con un comparador por nombre, la operacion dominante add() tiene complejidad 0(log n) y mantiene elementos ordenados de forma automatica.Descartamos un HashSet porq no garantiza ningun orden de recorrido 

Necesidad 5- Atender paquetes teniendo en cuanta primero la mayor prioridad 
Se eligio PriorityQueue<Paquete> ya que como debemos considerar primero q todo la mayor prioridad, luego el menor tiempo estimado y el codigo alfabetico, implementamos PriorityQueue<Paquete> con un comparador de despacho, con la operacion poll() extrae el elemento de mayor precedencia con complejidad 0(log n)

Necesidad 6- Mantener el historial de paquetes entregados en orden de entrega 
Por restriccion de el parcial la estructura tenia que ser desarrollada propia, se implemento una list enlazada propia generica que respeta el orden de insercion y puede recorrerse mediante Iterable e Iterator

Neecesidad 7- Asociar cada codigo de paquete con el objeto paquete correspondiente
Se eligio HashMap<String,Paquete> la operacion dominante put(codigo,paquete) tiene complejidad 0(1). }Esta misma estrcutura soporta la Necesidad 2 la asociacion codigo->paquete es la base sobre la cual se  realiza la busqueda eficiente por codigo

Métodos principales de Quindío Express

/Registro y validacion
*registrarPaquete(p) — valida nulo y código duplicado, y agrega a todas las colecciones
*registrarRepartidor(r) — valida nulo e id duplicado

/Consultas 
*consultarPaquete(id) — búsqueda O(1) por código (HashMap)
*consultarRepartidor(id) — búsqueda por id
*consultarPaquetesMunicipio(m)— imprime los paquetes de un municipio
*determinarMunicipiosCon1PaqueteRegistrado() y obtenerMunicipiosOrdenados()

/Ordenamientos
*ordenarCodigosPaquete() — orden natural (código)
*organizarPaquetesPrioridadDescendente() / organizarPaquetesPesoDescendente() / organizarPaquetesTiempoEstimado()

/Atención de paquetes 
*atenderPaquetePrioridad() — saca de la PriorityQueue
*atenderPaquetePorLlegada() — saca el primero de ordenRegistro
*verSiguientePaqueteDespacho() — peek() sin sacar

/Asignación y entrega 
*asignarPaqueteRepartidor(codigo, id) — valida que el repartidor esté disponible y que su zona coincida con el destino
*cambiarDisponibilidadRepartidor(id, disponible)
*RegistrarEntrega(codigo) — quita el paquete de las estructuras pendientes, libera al repartidor y lo guarda en el historial

/Algoritmos
*determinarTotalPesoPaquetes(m) — recursión lineal (suma de pesos por municipio)
*determinarNumeroPaquetePrioridadDada(min) — recursión lineal (conteo por prioridad)
*buscarPaqueteCodigo(ordenados, codigo) — búsqueda binaria (precondición: lista ordenada por código)
*determinarPesoMayorPaquete() — divide y vencerás para el mayor peso.





