# Auditoría del Proyecto — g1-agencia-viajes

**Fecha:** 13/05/2026
**Proyecto:** Agencia de Viajes — Backend (Spring Boot)
**Stakeholder:** Carla

---

## Resumen General

| Requisito | Estado |
|-----------|--------|
| Entidades JPA | ✅ 9 entidades |
| CRUD completo por entidad | ✅ Todas (Controller + Service + Repository) |
| DTOs (Request/Response) | ⚠️ Parcial (5/9 entidades) |
| Validaciones @Valid | ⚠️ Parcial (6/9 controllers) |
| Exception Handling | ✅ GlobalExceptionHandler (6 tipos) |
| Tarifas (niño/adulto/pensionista) | ✅ Implementado |
| Descuento grupo (IMSERSO/colegio) | ✅ 5% para ≥10 pax |
| Ofertas en viajes | ✅ Implementado |
| Menor acompañado tutor | ✅ MinorWithoutTutorException |
| JWT + BCrypt | ✅ Básico |
| CORS | ✅ Puerto 5173 |
| Validación fechas viaje | ✅ fin > inicio |
| **Cloudinary** | ❌ No existe |
| **Dashboard** | ❌ No existe |
| **Email** | ❌ No existe |
| **Capacidad autobús** | ❌ No implementado |
| **Capacidad hotel** | ⚠️ Métodos existen pero no se usan |
| **Conductor ocupado** | ⚠️ Query existe pero no se invoca |
| **Viajes pasados** | ⚠️ Parcial (falta validación en reserva) |
| **Seguridad** | ⚠️ Débil (secret hardcodeado, sin expiración, sin roles) |

---

## 1. Entidades (9 JPA)

| Entidad | Tabla | Campos clave |
|---------|-------|-------------|
| `User` | `users` | id, name, surname, email, dni, passport, age, tutorId (self-ref), active |
| `Hotel` | `hotels` | id, name, address, city, country, stars, capacity, availablePlaces, halfBoardPrice, fullBoardPrice, imageUrl, active |
| `Bus` | `buses` | id, licensePlate, capacity, bath, wifi, AC, USB |
| `Driver` | `drivers` | id, name, phone, licenceActive, imageUrl |
| `Travel` | `travels` | id, destiny, startDate, endDate, sale, active, availablePlaces, hotel (FK), offer (FK) |
| `TripSegment` | `trip_segments` | id, travel (FK), origin, destination, startTime, endTime, bus (FK), driver (FK) |
| `Booking` | `bookings` | bookingId, boughtDate, typeBoard (HALF/FULL), isGroup, totalPrice, travel (FK), employee (FK) |
| `Offer` | `offers` | offerId, discountPercentage, startDate, endDate |
| `Employee` | `employees` | employeeId, name, surname, gender, workHour, hired, password |

---

## 2. CRUD por Entidad

| Entidad | Controller | Endpoints | Service | Repository | DTOs |
|---------|-----------|-----------|---------|------------|------|
| User | ✅ | POST, GET, GET/{id}, GET/activos, PUT, DELETE | ✅ UserService | ✅ + `findByEmail`, `findByActive`, `existsByEmail` | ✅ UserRequestDTO / UserResponseDTO |
| Hotel | ✅ | POST, GET, GET/{id}, GET/activos, GET/disponibles, PUT, DELETE | ✅ HotelService | ✅ + `findByActive`, `findByCity`, `findByCountry`, `findByStars`, `findByAvailablePlacesGreaterThan` | ✅ HotelRequestDTO / HotelResponseDTO |
| Bus | ✅ | POST, GET, GET/{id}, PUT, DELETE | ✅ BusServiceImpl | ✅ + `existsByLicensePlate` | ✅ BusRequestDTO / BusResponseDTO |
| Driver | ✅ | POST, GET, GET/{id}, GET/activos, PUT, DELETE | ✅ DriverService | ✅ + `findByLicenceActive` | ✅ DriverRequestDTO / DriverResponseDTO |
| Travel | ✅ | POST, GET, GET/available, GET/sale, GET/{id}, PUT, DELETE | ✅ TravelService | ✅ + `findByActiveTrue`, `findBySaleTrueAndActiveTrueAndStartDateAfter`, `findByActiveTrueAndStartDateAfter` | ✅ TravelRequestDTO / TravelResponseDTO |
| Booking | ✅ | POST, GET, GET/{id}, POST/quote, PUT, DELETE | ✅ BookingService | ✅ (solo JPA básico) | ⚠️ Usa entidad cruda para create/update, DTO solo para quote |
| TripSegment | ✅ | POST, GET, GET/{id}, PUT, DELETE | ✅ TripSegmentService | ✅ + `findOverlappingByDriver` | ❌ Usa entidad cruda |
| Offer | ✅ | POST, GET, GET/{id}, PUT, DELETE | ✅ OfferService | ✅ (solo JPA básico) | ❌ Usa entidad cruda |
| Employee | ✅ | POST, GET, GET/{id}, PUT, DELETE | ✅ EmployeeService | ✅ (solo JPA básico) | ❌ Usa entidad cruda |

---

## 3. DTOs

### Request DTOs (8)
- `UserRequestDTO` — name, surname, email, dni, passport, age, tutorId, active
- `HotelRequestDTO` — name, address, city, country, stars, capacity, availablePlaces, halfBoardPrice, fullBoardPrice, imageUrl, active
- `BusRequestDTO` — licensePlate, capacity, bath, wifi, AC, USB, available
- `DriverRequestDTO` — name, phone, enrolment, licenceActive, imageUrl
- `TravelRequestDTO` — destiny, startDate, endDate, sale, availablePlaces, hotelId
- `BookingQuoteRequestDTO` — travelId, typeBoard, isGroup, customerIds
- `BookingUserRequestDTO` — bookingId, userId
- `LoginRequest` — id, password

### Response DTOs (7)
- `UserResponseDTO` — userId, fullName, id, name, surname, email, dni, passport, age, tutorId, active
- `HotelResponseDTO` — id, name, address, city, country, stars, capacity, availablePlaces, halfBoardPrice, fullBoardPrice, imageUrl, active
- `BusResponseDTO` — id, licensePlate, capacity, bath, wifi, AC, USB, available
- `DriverResponseDTO` — id, name, phone, enrolment, licenceActive, imageUrl
- `TravelResponseDTO` — id, destiny, startDate, endDate, sale, availablePlaces, active, hotelId, hotelName, hotelCity, hotelCountry, hotelImageUrl, halfBoardPrice, fullBoardPrice
- `BookingQuoteResponseDTO` — travelId, travelDestiny, typeBoard, isGroup, passengers, basePricePerPassenger, totalBeforeDiscount, totalDiscount, totalPrice, passengerDetails
- `BookingQuotePassengerDetailDTO` — userId, fullName, age, category, basePrice, offerDiscountAmount, categoryDiscountAmount, finalPrice
- `LoginResponse` — token, employeeId, name, surname

---

## 4. Validaciones

### Controllers con @Valid
| Controller | @Valid en create | @Valid en update |
|------------|:----------------:|:----------------:|
| UserController | ✅ | ✅ |
| HotelController | ✅ | ✅ |
| BusController | ✅ | ✅ |
| DriverController | ✅ | ✅ |
| TravelController | ✅ | ✅ |
| BookingController | ✅ | ✅ |
| TripSegmentController | ✅ | ✅ |
| **OfferController** | ❌ | ❌ |
| **EmployeeController** | ❌ | ❌ |
| **AuthenticationController** | ❌ | ❌ |

### Anotaciones de validación usadas
- `@NotBlank` — nombre, email, matrícula, etc.
- `@NotNull` — capacidad, fechas, precios, etc.
- `@Email` — email de usuario
- `@Min` — edad, estrellas, capacidad, precios
- `@Future` — startDate, endDate en TravelRequestDTO
- `@Pattern` — DNI (regex: `^[0-9]{8}[A-Z]$`)
- `@DecimalMin` / `@DecimalMax` — descuento oferta (0-100)
- `@NotEmpty` — customerIds en BookingQuoteRequestDTO

---

## 5. Exception Handling

### GlobalExceptionHandler (@RestControllerAdvice)
| Excepción | HTTP Status |
|-----------|:-----------:|
| `MethodArgumentNotValidException` | 400 BAD_REQUEST |
| `ResourceNotFoundException` | 404 NOT_FOUND |
| `HotelNotAvailableException` | 409 CONFLICT |
| `EmailAlreadyExistsException` | 409 CONFLICT |
| `IllegalArgumentException` | 400 BAD_REQUEST |
| `MinorWithoutTutorException` | 400 BAD_REQUEST |

### Excepciones personalizadas (4)
- `ResourceNotFoundException` — "No hemos podido encontrar la información de {name}, con el id: {id}"
- `EmailAlreadyExistsException` — "Ya existe un usuario con el email: {email}"
- `HotelNotAvailableException` — "Hotel con id: {id} no tiene plazas disponibles"
- `MinorWithoutTutorException` — "Un menor de edad debe ir acompañado de un tutor para crear la reserva"

---

## 6. Lógica de Negocio

### 6.1 Tarifas (BookingPricingService) ✅
- **Niño** (<18): 15% descuento
- **Adulto** (18-64): Sin descuento
- **Pensionista** (≥65): 10% descuento
- Categorías: CHILD, ADULT, PENSIONER, UNKNOWN

### 6.2 Descuento por grupo ✅
- 5% adicional si `isGroup = true` y ≥10 pasajeros
- Aplicado sobre el precio total tras descuentos individuales

### 6.3 Ofertas en viajes ✅
- `travel.sale = true` + `travel.offer.discountPercentage`
- Descuento porcentual sobre precio base
- Se aplica ANTES del descuento por categoría

### 6.4 Múltiples pasajeros ✅
- `BookingQuoteRequestDTO.customerIds` como lista
- `BookingService.save()` resuelve y valida cada cliente
- `addCustomerToBooking()` añade progresivamente

### 6.5 Menor acompañado ❗
- `user.age < 18 && user.tutorId == null` → MinorWithoutTutorException
- Validado en `save()`, `update()`, `addCustomerToBooking()`
- ✅ **Implementado correctamente**

### 6.6 Viajes pasados ⚠️
- `@Future` en TravelRequestDTO (startDate, endDate) ✅
- `TravelService.getAvailable()` filtra `startDate.isAfter(LocalDate.now())` ✅
- **Booking no valida que el viaje no haya pasado** ❌

### 6.7 Capacidad autobús ❌
- **No implementado.** El campo `capacity` existe en Bus pero nunca se compara contra el número de pasajeros.

### 6.8 Capacidad hotel ⚠️
- `HotelService.reducirPlazas()` y `liberarPlazas()` existen
- **Pero nunca se llaman desde BookingService** — hay un gap de integración

### 6.9 Conductor ocupado ⚠️
- `TripSegmentRepository.findOverlappingByDriver()` existe con query personalizada
- **Pero nunca se invoca** en el flujo de crear/actualizar TripSegment

### 6.10 Email tras compra ❌
- **No existe.** Sin dependencia mail, sin configuración, sin servicio.

---

## 7. Cloudinary ❌

**No implementado.** Sin dependencia en `pom.xml`, sin configuración, sin servicio de subida.
Varias entidades tienen campo `imageUrl` pero no hay forma de subir imágenes.

---

## 8. Dashboard ❌

**No implementado.** Sin endpoints para:
- Viajes organizados por año
- Ganancias por año
- Top 3 viajes que más recaudan
- BookingRepository solo tiene métodos JPA básicos

---

## 9. Email ❌

**No implementado.**
- Sin `spring-boot-starter-mail` en pom.xml
- Sin JavaMailSender
- Sin servicio de email
- Sin configuración SMTP

---

## 10. Seguridad ⚠️

### Implementado
- JWT con `auth0/java-jwt` (HMAC256)
- Filtro `JwtFilter` en `/api/*` (excepto `/api/authentication/login`)
- BCrypt para hash de contraseñas
- CORS configurado para `localhost:5173`

### Debilidades
| Problema | Detalle |
|----------|---------|
| Secret hardcodeado | `"your_secret_password"` en JwtUtil.java y JwtFilter.java |
| Sin expiración | Los tokens no tienen fecha de expiración |
| Sin roles | No hay control de acceso por rol |
| Contraseñas visibles | Employee entity expone password en respuestas API |
| Sin HTTPS | No hay configuración de seguridad de transporte |

---

## Brechas Prioritarias

| Prioridad | Brecha | Impacto |
|:---------:|--------|---------|
| 🔴 1 | **Cloudinary** | OBLIGATORIO según briefing |
| 🔴 2 | **Email tras compra** | Stakeholder espera email detallado |
| 🔴 3 | **Dashboard dirección** | Analytics anuales requeridos |
| 🟡 4 | **Capacidad bus/hotel** | Criterios de aceptación: no vender si completo |
| 🟡 5 | **Conductor no ocupado** | Criterio de aceptación |
| 🟢 6 | **DTOs pendientes** | Booking, TripSegment, Offer, Employee |
| 🟢 7 | **Seguridad** | Hardening (secret, expiración, roles) |
