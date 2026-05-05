# ✈️ Backend — Agencia de Viajes

API REST desarrollada con **Spring Boot** para la gestión completa de una agencia de viajes. Permite administrar usuarios, hoteles, autobuses, conductores, viajes y reservas, con soporte para lógica de negocio avanzada como tarifas por edad, descuentos de grupo, validación de disponibilidad y envío de emails de confirmación.

---

## 📋 Tabla de contenidos

- [Tecnologías](#tecnologías)
- [Requisitos previos](#requisitos-previos)
- [Instalación y configuración](#instalación-y-configuración)
- [Estructura del proyecto](#estructura-del-proyecto)
- [Entidades](#entidades)
- [Endpoints de la API](#endpoints-de-la-api)
- [Ejemplos de peticiones](#ejemplos-de-peticiones)
- [Reglas de negocio](#reglas-de-negocio)
- [Ramas del repositorio](#ramas-del-repositorio)
- [Equipo](#equipo)

---

## 🛠️ Tecnologías

| Tecnología | Versión |
|---|---|
| Java | 17 |
| Spring Boot | 3.2.5 |
| Spring Data JPA | — |
| Spring Validation | — |
| MySQL | 8+ |
| Lombok | — |
| Maven | — |
| Cloudinary | — |
| JavaMailSender | — |

---

## ✅ Requisitos previos

- Java 17 instalado
- MySQL 8+ en ejecución
- Maven instalado
- Cuenta en Cloudinary (para subida de imágenes de hoteles)
- Cuenta de correo para envío de emails (Gmail recomendado)

---

## ⚙️ Instalación y configuración

### 1. Clonar el repositorio

```bash
git clone https://github.com/Agencia-de-viajes-PF-Factoria-F5/Backend-Travel-Agency.git
cd Backend-Travel-Agency
```

### 2. Crear la base de datos

```sql
CREATE DATABASE agencia_viajes;
```

### 3. Configurar `application.properties`

Edita el archivo `src/main/resources/application.properties`:

```properties
# Base de datos
spring.datasource.url=jdbc:mysql://localhost:3306/agencia_viajes
spring.datasource.username=TU_USUARIO
spring.datasource.password=TU_PASSWORD
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

# Cloudinary
cloudinary.cloud-name=TU_CLOUD_NAME
cloudinary.api-key=TU_API_KEY
cloudinary.api-secret=TU_API_SECRET

# Email
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=TU_EMAIL
spring.mail.password=TU_APP_PASSWORD
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

# Puerto
server.port=8080
```

### 4. Arrancar el proyecto

```bash
mvn spring-boot:run
```

La API estará disponible en `http://localhost:8080`

---

## 📁 Estructura del proyecto

```
src/main/java/com/inditex/g1_agencia_viajes/
├── controller/
│   ├── UserController.java
│   ├── HotelController.java
│   ├── BusController.java
│   ├── DriverController.java
│   ├── TravelController.java
│   └── BookingController.java
├── service/
│   ├── UserService.java
│   ├── HotelService.java
│   ├── BusService.java
│   ├── DriverService.java
│   ├── TravelService.java
│   └── BookingService.java
├── repository/
│   ├── UserRepository.java
│   ├── HotelRepository.java
│   ├── BusRepository.java
│   ├── DriverRepository.java
│   ├── TravelRepository.java
│   └── BookingRepository.java
├── model/
│   ├── User.java
│   ├── Hotel.java
│   ├── Bus.java
│   ├── Driver.java
│   ├── Travel.java
│   ├── Booking.java
│   ├── TripSegment.java
│   └── CustomerBooking.java
├── dto/
│   ├── UserRequestDTO.java / UserResponseDTO.java
│   ├── HotelRequestDTO.java / HotelResponseDTO.java
│   ├── BusRequestDTO.java / BusResponseDTO.java
│   ├── DriverRequestDTO.java / DriverResponseDTO.java
│   ├── TravelRequestDTO.java / TravelResponseDTO.java
│   └── BookingRequestDTO.java / BookingResponseDTO.java
├── mapper/
│   ├── UserMapper.java
│   ├── HotelMapper.java
│   ├── BusMapper.java
│   ├── DriverMapper.java
│   ├── TravelMapper.java
│   └── BookingMapper.java
├── exception/
│   ├── UserNotFoundException.java
│   ├── HotelNotFoundException.java
│   ├── BusNotFoundException.java
│   ├── DriverNotFoundException.java
│   ├── TravelNotFoundException.java
│   └── BookingNotFoundException.java
└── G1AgenciaViajesApplication.java
```

---

## 🗃️ Entidades

### Users
| Campo | Tipo | Descripción |
|---|---|---|
| id | Long | Identificador único |
| name | String | Nombre |
| surname | String | Apellido |
| email | String | Email único |
| password | String | Contraseña |
| dni | String | DNI |
| passport | String | Pasaporte |
| age | Integer | Edad |
| tutorId | Long | ID del tutor (menores) |
| active | Boolean | Soft delete |
| rol | Enum | ADMIN / USER |

### Hotels
| Campo | Tipo | Descripción |
|---|---|---|
| id | Long | Identificador único |
| name | String | Nombre del hotel |
| address | String | Dirección |
| city | String | Ciudad |
| country | String | País |
| stars | Integer | Estrellas (1-5) |
| capacity | Integer | Capacidad total |
| availablePlaces | Integer | Plazas disponibles |
| halfBoardPrice | Double | Precio media pensión |
| fullBoardPrice | Double | Precio pensión completa |
| imageUrl | String | URL imagen (Cloudinary) |
| active | Boolean | Soft delete |

### Buses
| Campo | Tipo | Descripción |
|---|---|---|
| id | Long | Identificador único |
| totalPlaces | Integer | Plazas totales |
| enrollment | String | Matrícula |

### Drivers
| Campo | Tipo | Descripción |
|---|---|---|
| id | Long | Identificador único |
| name | String | Nombre |
| phone | Integer | Teléfono |
| licenceActive | Boolean | Licencia en vigor |
| rol | Enum | Rol del conductor |

### Travels
| Campo | Tipo | Descripción |
|---|---|---|
| id | Long | Identificador único |
| destiny | String | Destino |
| startDate | LocalDate | Fecha de inicio |
| endDate | LocalDate | Fecha de fin |
| sale | Boolean | En oferta |
| availablePlaces | Integer | Plazas disponibles |
| active | Boolean | Soft delete |
| hotel | Hotel (FK) | Hotel asociado |

### Bookings
| Campo | Tipo | Descripción |
|---|---|---|
| id | Long | Identificador único |
| boughtDate | Timestamp | Fecha de compra |
| typeBoard | Enum | HALF_BOARD / FULL_BOARD |
| group | Boolean | Descuento de grupo |
| totalPrice | Double | Precio total |
| travel | Travel (FK) | Viaje reservado |

### TripSegments
| Campo | Tipo | Descripción |
|---|---|---|
| id | Long | Identificador único |
| origin | String | Origen del trayecto |
| destination | String | Destino del trayecto |
| startTime | Date | Fecha/hora de salida |
| endTime | Date | Fecha/hora de llegada |
| travel | Travel (FK) | Viaje asociado |
| bus | Bus (FK) | Autobús asignado |
| driver | Driver (FK) | Conductor asignado |

### CustomerBookings
| Campo | Tipo | Descripción |
|---|---|---|
| booking | Booking (FK) | Reserva |
| customer | User (FK) | Usuario o acompañante |
| isUser | Boolean | Es el comprador principal |

---

## 🔌 Endpoints de la API

### Users — `/api/users`
| Método | Endpoint | Descripción |
|---|---|---|
| POST | `/api/users` | Crear usuario |
| GET | `/api/users` | Listar todos los usuarios |
| GET | `/api/users/{id}` | Obtener usuario por ID |
| GET | `/api/users/activos` | Listar usuarios activos |
| PUT | `/api/users/{id}` | Actualizar usuario |
| DELETE | `/api/users/{id}` | Eliminar usuario (soft delete) |

### Hotels — `/api/hotels`
| Método | Endpoint | Descripción |
|---|---|---|
| POST | `/api/hotels` | Crear hotel |
| GET | `/api/hotels` | Listar todos los hoteles |
| GET | `/api/hotels/{id}` | Obtener hotel por ID |
| GET | `/api/hotels/available` | Hoteles con plazas disponibles |
| PUT | `/api/hotels/{id}` | Actualizar hotel |
| DELETE | `/api/hotels/{id}` | Eliminar hotel (soft delete) |

### Buses — `/api/buses`
| Método | Endpoint | Descripción |
|---|---|---|
| POST | `/api/buses` | Crear autobús |
| GET | `/api/buses` | Listar todos los autobuses |
| GET | `/api/buses/{id}` | Obtener autobús por ID |
| PUT | `/api/buses/{id}` | Actualizar autobús |
| DELETE | `/api/buses/{id}` | Eliminar autobús |

### Drivers — `/api/drivers`
| Método | Endpoint | Descripción |
|---|---|---|
| POST | `/api/drivers` | Crear conductor |
| GET | `/api/drivers` | Listar todos los conductores |
| GET | `/api/drivers/{id}` | Obtener conductor por ID |
| PUT | `/api/drivers/{id}` | Actualizar conductor |
| DELETE | `/api/drivers/{id}` | Eliminar conductor |

### Travels — `/api/travels`
| Método | Endpoint | Descripción |
|---|---|---|
| POST | `/api/travels` | Crear viaje |
| GET | `/api/travels` | Listar todos los viajes |
| GET | `/api/travels/{id}` | Obtener viaje por ID |
| GET | `/api/travels/available` | Viajes futuros con plazas disponibles |
| GET | `/api/travels/sale` | Viajes en oferta |
| PUT | `/api/travels/{id}` | Actualizar viaje |
| DELETE | `/api/travels/{id}` | Eliminar viaje (soft delete) |

### Bookings — `/api/bookings`
| Método | Endpoint | Descripción |
|---|---|---|
| POST | `/api/bookings` | Crear reserva |
| GET | `/api/bookings` | Listar todas las reservas |
| GET | `/api/bookings/{id}` | Obtener reserva por ID |
| PUT | `/api/bookings/{id}` | Actualizar reserva |
| DELETE | `/api/bookings/{id}` | Cancelar reserva |

---

## 📦 Ejemplos de peticiones

### Crear un viaje
```json
POST /api/travels
{
  "destiny": "París",
  "startDate": "2025-07-01",
  "endDate": "2025-07-10",
  "sale": true,
  "availablePlaces": 40,
  "hotelId": 1
}
```

### Crear un usuario
```json
POST /api/users
{
  "name": "María",
  "surname": "López",
  "email": "maria@email.com",
  "password": "password123",
  "dni": "12345678A",
  "age": 35,
  "rol": "USER"
}
```

### Crear un hotel
```json
POST /api/hotels
{
  "name": "Hotel Paris Centre",
  "address": "12 Rue de Rivoli",
  "city": "París",
  "country": "Francia",
  "stars": 4,
  "capacity": 100,
  "availablePlaces": 80,
  "halfBoardPrice": 85.00,
  "fullBoardPrice": 120.00
}
```

### Crear una reserva
```json
POST /api/bookings
{
  "travelId": 1,
  "userId": 3,
  "typeBoard": "FULL_BOARD",
  "group": false,
  "companions": [
    { "name": "Ana", "surname": "García", "age": 35 },
    { "name": "Luis", "surname": "García", "age": 8 }
  ]
}
```

---

## ⚖️ Reglas de negocio

- No se puede reservar un viaje si el **autobús está completo**
- No se puede reservar un viaje si el **hotel está completo**
- No se pueden vender **viajes con fecha pasada**
- Un **menor no puede viajar sin un adulto** acompañante
- Un **conductor no puede conducir dos autobuses al mismo tiempo**
- El autobús cubre únicamente el **trayecto de ida y vuelta** del viaje; los desplazamientos intermedios entre hoteles no están cubiertos
- Existen **tarifas diferenciadas**: niño, adulto y pensionista
- Se aplica **descuento por grupo** para colectivos como el Imserso o colegios
- El cliente recibe un **email detallado** con la información completa del viaje tras la compra

---

## 🌿 Ramas del repositorio

| Rama | Descripción |
|---|---|
| `main` | Código en producción |
| `develop` | Integración de features |
| `feature/travel` | CRUD de viajes |
| `feature/users` | CRUD de usuarios |
| `feat/userCRUD` | CRUD de usuarios (rama activa) |
| `feature/bookingsCRUD` | CRUD de reservas |
| `feature/crudbus` | CRUD de autobuses |
| `feature/drivers` | CRUD de conductores |
| `feature/hotels` | CRUD de hoteles |
| `feature/cors-config` | Configuración CORS |
| `feature/corsconfig` | Configuración CORS (alternativa) |

---

## 👥 Equipo

Proyecto desarrollado por el **Grupo 1** de **Factoría F5** — Proyecto Final 2025.

---

## 📄 Licencia

Este proyecto es de uso educativo desarrollado en el bootcamp de **Factoría F5**.
