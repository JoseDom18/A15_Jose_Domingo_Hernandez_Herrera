# A15_Jose_Domingo_Hernandez_Herrera

Repositorio creado para el laboratorio del bloque 4 del curso de Axity

## Herramientas usadas
* JAVA 17
* Maven
* H2 y liquibase
* JUnit 5 + Mockito + JaCoCo

## Patrones de diseño 
* Singleton: se uso para la parte del manejo de la configuraciones ParkConfig ya que las
configuraciones necesitan ser únicas en todo el proyecto por los tanto con el patron
singleton se evita la creación innecesaria de objetos, esto lo logramos haciendo
el constructor de la clase privado y creando un método que nos permita instanciar solo
si no existe actualmente una instancia que se guarda en la misma clase si ya existe
solo se regresa esta misma instancia ya creada generando una unica fuente de 
configuraciones para todo el proyecto. Para acceder a las configuraciones se crean
métodos que nos permitan regresar las propiedades que fueron definidas en el archivo
**src/main/resources/park.properties** que maven nos ayuda a leer.
* Strategy: 

## Flujo de simulación
```mermaid
graph TD
    A[Inicio del Step de Simulación] --> B{¿Hay turistas esperando?}
    B -- Sí --> C[ArrivalZone: Venta de boletos e Ingreso]
    B -- No --> D[Central Hub: Distribuir turistas activos]
    C --> D
    D --> E[Tick en Baños y Encierros]
    E --> F[PowerPlant: Consumir Energía]
    F --> G{¿Se dispara evento aleatorio?}
    G -- Sí --> H[Ejecutar Evento: Apagón, Escape, Tormenta...]
    G -- No --> I[Workers: Guardias y Técnicos operan]
    H --> I
    I --> J[Monitor: Imprimir estado del parque]
    J --> K{¿Se alcanzó el totalSteps?}
    K -- No --> A
    K -- Sí --> L[Fin de Simulación y Cierre de BD]
 ```



Dudas:
* la interfaz ParkZone se debe implementar en todas las zonas, ya que la zona 
 Power Plant no tiene acceso a turistas o simplemente la dejo en 0.
* El método tryEnter de bathroom no es el mismo que enter de la interfaz?
* El método repair de technician necesita registrarse por lo tanto necesita un CsvWriter
* 