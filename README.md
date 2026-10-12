QUINDIO EXPRESS

Introducción

QuindioExpress una empresa que presta servicios de recepcion,clasificacion y distribución de paquetes hacia diferentes municipios del departamento del Quindio.Cada dia el centro de distribuccion recibe una cantidad sonsiderable de paquetes provenientes de distintos remitentes, los cuales ingresan al sistema en el mismo orden en que llegan, y pasan por diferentes procesos: 
-Registro
-Consulta
-Priorizacion
-Asignacion para despacho y registro de entrega

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




