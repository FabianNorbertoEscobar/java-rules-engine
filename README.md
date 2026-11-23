# Motor de reglas en Java

Motor sencillo y encadenable para evaluar reglas de negocio. Este repositorio es una práctica/base de proyecto: contiene la API principal, ejemplos de reglas, pruebas unitarias y configuración de logging; no es una solución completa lista para producción.

## Stack y herramientas

- Java 11.
- Maven para dependencias, compilación y ejecución de pruebas.
- JUnit Jupiter 5 para las pruebas (el `pom.xml` también declara JUnit 3.8.1).
- Log4j 2 para logging, con la implementación SLF4J y configuración YAML.
- Lombok para generar el constructor y los getters de `RuleResult`.
- Jakarta REST (JAX-RS) y Vert.x Core usados por el filtro de correlación HTTP y la serialización JSON del logging.
- JaCoCo y Sonar Maven Scanner disponibles mediante el perfil opcional `coverage`.

## Funcionalidades implementadas

- Contrato `IRule` para definir una comprobación, el mensaje de éxito y el mensaje de error.
- `RuleEngine.evaluate(...)` instancia una regla y le asigna valores desde un mapa, usando los nombres de sus campos, antes de ejecutar `check()`.
- `RuleResult.and(...)` y `or(...)` permiten encadenar reglas con evaluación de cortocircuito.
- `fire()` lanza `RuleException` cuando el resultado es falso.
- Variantes `ifThen` e `ifThenOrElse` aceptan `Consumer`, `Function` o `Callable`.
- El filtro `CorrelationIdFilter` conserva el encabezado `X-Correlation-ID` o genera uno, lo añade al contexto de logging y lo devuelve en la respuesta.
- Logging de consola con un patrón que incluye fecha, nivel y correlation ID. También hay layouts/mensajes JSON personalizados.

## Estructura

```text
src/
├── main/
│   ├── java/com/example/rules/engine/       # API y motor
│   ├── java/com/example/rules/engine/log/   # Logging y correlación HTTP
│   ├── java/com/example/rules/engine/exception/
│   └── resources/log4j2.yaml
└── test/java/com/example/rules/engine/      # Pruebas y reglas de ejemplo
```

## Requisitos y comandos

Se requiere JDK 11 o superior y Maven.

```bash
mvn test
mvn package
```

Para generar el informe JaCoCo con el perfil configurado:

```bash
mvn verify -Pcoverage
```

El perfil de cobertura también ejecuta el scanner de Sonar; para utilizarlo hace falta configurar un servidor y credenciales Sonar en el entorno de desarrollo/CI.

## Ejemplo

Una regla debe tener constructor sin argumentos y sus campos configurables deben coincidir con las claves del mapa:

```java
RuleResult result = RuleEngine.evaluate(
    MiRegla.class,
    Map.of("importe", 125, "habilitada", true)
);

result.and(OtraRegla.class, Map.of("clienteActivo", true))
      .ifThen(operacion, op -> procesar(op));
```

## Alcance y estado

Es una base funcional para practicar composición y ejecución de reglas, no un motor de reglas general ni una aplicación ejecutable por sí sola. La cobertura de pruebas es limitada y se concentra en el encadenamiento y las acciones condicionales. Antes de usarlo en producción convendría añadir validación explícita de parámetros y tipos, manejo de reglas que devuelvan `null`, pruebas de errores y del filtro HTTP, y una integración de ejemplo que defina el runtime JAX-RS/Vert.x. La inyección actual usa reflexión sobre campos y requiere que los nombres de parámetros coincidan exactamente.
