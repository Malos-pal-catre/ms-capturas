# ms-capturas

Microservicio encargado del registro de capturas de pesca artesanal en la **Caleta Lo Abarca**. Es el núcleo operativo del sistema, donde se registra la llegada de cada bote con su carga del día. Forma parte del sistema de gestión de subasta artesanal desarrollado con arquitectura de microservicios Spring Boot.

## ¿Qué hace?

Gestiona el registro diario de capturas. Cuando un pescador llega a la caleta al amanecer con su carga, este microservicio registra la especie, los kilos capturados, la embarcación y el pescador. Antes de guardar cualquier captura, se comunica con `ms-especies` via **WebClient** para validar que la especie no tenga veda activa y que haya cuota Sernapesca suficiente. Si ambas condiciones se cumplen, descuenta automáticamente los kilos de la cuota disponible y guarda la captura con estado `PENDIENTE`, dejándola lista para entrar a subasta.

## Comunicación con otros microservicios

Este microservicio utiliza **WebClient** para comunicarse con:

| Microservicio | Puerto | Para qué |
|---|---|---|
| `ms-especies` | 8083 | Validar veda, verificar cuota, descontar kilos |

## Endpoints

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/capturas` | Lista todas las capturas |
| GET | `/api/capturas/{id}` | Busca una captura por ID |
| GET | `/api/capturas/pescador/{pescadorId}` | Lista capturas de un pescador |
| GET | `/api/capturas/pendientes` | Lista capturas esperando subasta |
| GET | `/api/capturas/fecha?fecha=` | Lista capturas por fecha |
| POST | `/api/capturas` | Registra una nueva captura |
| PATCH | `/api/capturas/{id}/estado?nuevoEstado=` | Cambia el estado de una captura |
| DELETE | `/api/capturas/{id}` | Elimina una captura |

## Estados de una captura

| Estado | Descripción |
|---|---|
| `PENDIENTE` | Recién registrada, esperando entrar a subasta |
| `EN_SUBASTA` | Actualmente siendo subastada |
| `VENDIDO` | Ya fue adjudicada a un comprador |

## Ejemplo de uso

**Registrar captura:**
```json
POST /api/capturas
{
  "pescadorId": 1,
  "embarcacionId": 1,
  "especieId": 1,
  "kgTotal": 80.0,
  "fecha": "2026-05-30"
}
```

**Respuesta:**
```json
{
  "id": 1,
  "pescadorId": 1,
  "embarcacionId": 1,
  "especieId": 1,
  "nombreEspecie": "Congrio",
  "kgTotal": 80.0,
  "fecha": "2026-05-30",
  "estado": "PENDIENTE"
}
```

## Validaciones de negocio

- La especie no puede tener veda activa al momento de registrar la captura
- Los kilos capturados no pueden superar la cuota disponible de la especie
- Al registrar, se descuenta automáticamente la cuota en `ms-especies`

## Tecnologías

- Java 21
- Spring Boot 3.4.5
- Spring Data JPA
- Spring Boot Validation
- Spring Reactive Web (WebClient)
- PostgreSQL (Neon)
- Lombok

## Configuración

Crear el archivo `src/main/resources/application.properties` con:

```properties
spring.application.name=ms-capturas
server.port=8084

spring.datasource.url=jdbc:postgresql://<HOST>/capturas_db?sslmode=require&channelBinding=require
spring.datasource.username=<USUARIO>
spring.datasource.password=<PASSWORD>
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect

app.ms-especies.url=http://localhost:8083
```

## Dependencias requeridas corriendo

Para funcionar correctamente este microservicio necesita que estén activos:
- `ms-especies` en el puerto `8083`

## Cómo correr

```bash
mvnw.cmd spring-boot:run
```

El servicio queda disponible en `http://localhost:8084`

## Estructura del proyecto

```
ms-capturas/
├── client/        → EspecieClientService (WebClient hacia ms-especies)
├── config/        → WebClientConfig
├── controller/    → CapturaController (endpoints REST)
├── service/       → CapturaService (lógica de negocio + validaciones)
├── repository/    → CapturaRepository (JPA + @Query)
├── model/         → Captura (entidad JPA)
├── dto/           → RequestDTO, ResponseDTO, EspecieResponseDTO, Mapper
└── exception/     → GlobalExceptionHandler, RecursoNoEncontradoException
```

## Parte del sistema

Este microservicio es parte del sistema **Caleta Lo Abarca** junto a:
`ms-pescadores` · `ms-embarcaciones` · `ms-especies` · `ms-auth` · `ms-subastas` · `ms-compradores` · `ms-pagos` · `ms-bodega` · `ms-vedas` · `ms-reportes`
