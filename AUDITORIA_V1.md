# Auditoría del Proyecto — g1-agencia-viajes (V1 — Plan Original)

> Esta es la versión original de la auditoría con el plan de 4-5 semanas.
> La versión actualizada con el plan para 10 días está en `AUDITORIA.md`.

## Índice

1. [Resumen Ejecutivo](#1-resumen-ejecutivo)
2. [Entidades vs Requisitos](#2-entidades-vs-requisitos)
3. [Diagrama de Flujo vs Implementación](#3-diagrama-de-flujo-vs-implementación)
4. [Requisitos del Briefing vs Implementación](#4-requisitos-del-briefing-vs-implementación)
5. [Código Muerto y Problemas Detectados](#5-código-muerto-y-problemas-detectados)
6. [Discrepancias README vs Código Real](#6-discrepancias-readme-vs-código-real)
7. [Estado de los Sprints](#7-estado-de-los-sprints)
8. [Plan de Acción Exhaustivo](#8-plan-de-acción-exhaustivo)

---

## 1. Resumen Ejecutivo

| Aspecto | Estado |
|---|---|
| **Backend (Spring Boot)** | En progreso — estructura sólida pero con carencias críticas |
| **Frontend (React)** | **No existe** en este repositorio |
| **Autenticación** | ❌ No implementada |
| **Dashboard** | ❌ No implementado |
| **Email** | ❌ No implementado |
| **Pago** | ❌ No implementado |
| **Cloudinary** | ❌ No implementado |
| **Tests** | ❌ Solo test de contexto vacío |
| **Lógica de negocio core** | ✅ Mayoría implementada (tarifas, descuentos, menores) |
| **Validaciones críticas incompletas** | ⚠️ Autobús, conductor ocupado |

---

## 2. Entidades vs Requisitos

| Requisito | Estado | Notas |
|---|---|---|
| 4 entidades principales (Usuario, Hotel, Autobús, Conductor) | ✅ OK | Modelos: User, Hotel, Bus, Driver |
| CRUD completo para Usuario | ✅ OK | POST/GET/PUT/DELETE en `/api/users` |
| CRUD completo para Hotel | ✅ OK | POST/GET/PUT/DELETE en `/api/hotels` |
| CRUD completo para Autobús | ✅ OK | POST/GET/PUT/DELETE en `/api/buses` |
| CRUD completo para Conductor | ✅ OK | POST/GET/PUT/DELETE en `/api/drivers` |
| Entidades adicionales | ✅ OK | Travel, Booking, TripSegment, Employee, Offer |

---

## 3. Diagrama de Flujo vs Implementación

### 3.1 Inicio y Autenticación

| Paso del Diagrama | Implementación | Estado |
|---|---|---|
| Login / Registro | No hay endpoint de login ni registro con autenticación | ❌ |
| Rol Admin vs Usuario | El modelo User no tiene campo `rol` | ❌ |
| Dashboard Administrador | No existe ningún endpoint de dashboard | ❌ |

### 3.2 Selección de Operación y Oferta

| Paso del Diagrama | Implementación | Estado |
|---|---|---|
| Ver reserva (historial) | `GET /api/bookings` lista todas, pero sin filtro por usuario | ⚠️ |
| Nuevo viaje → Ver ofertas | `GET /api/travels/sale` devuelve viajes en oferta con precios | ✅ |
| Ver ofertas con precios | TravelResponseDTO incluye halfBoardPrice y fullBoardPrice | ✅ |
| Seleccionar nº de plazas | `availablePlaces` en Travel; validado al crear booking | ✅ |

### 3.3 Gestión de Acompañantes (Bucle)

| Paso del Diagrama | Implementación | Estado |
|---|---|---|
| ¿Añadir acompañante? | Booking acepta lista de customers (User IDs) | ✅ |
| Introducir datos del acompañante | Se pasan IDs de User existentes | ✅ |
| ¿Es menor de edad? | `validateCustomersForBooking()` comprueba edad < 18 | ✅ |
| Solicitar tutor + documentación | `User.tutorId` self-reference; tutor obligatorio para minors | ✅ |
| ¿Docs OK? → ERROR / continuar | Se valida que tutor exista; documentación física no modelada | ⚠️ |
| ¿Más acompañantes? | Bucle manejado desde frontend | ✅ |

### 3.4 Tarifas y Descuentos

| Paso del Diagrama | Implementación | Estado |
|---|---|---|
| Calcular tarifa total (Base × pensión × perfil) | `BookingPricingService.calculateTotalPrice()` | ✅ |
| ¿Grupo / Excursión / IMSERSO? | `isGroup=true` + ≥10 viajeros | ✅ |
| Aplicar descuento de grupo | 5% adicional sobre total | ✅ |
| Precio total desglosado | BookingQuoteResponseDTO con desglose por pasajero | ✅ |
| Tarifa niño (≤17) | 15% descuento sobre base | ✅ |
| Tarifa adulto | 0% descuento por categoría | ✅ |
| Tarifa pensionista (≥65) | 10% descuento sobre base | ✅ |

### 3.5 Validaciones Críticas

| Paso del Diagrama | Implementación | Estado |
|---|---|---|
| ¿Autobús disponible? | **No se valida** la capacidad del bus al reservar | ❌ |
| ¿Hotel disponible? | `HotelService.reducirPlazas()` controla disponibilidad | ✅ |
| ¿Fechas válidas? | `@Future` en startDate/endDate; viajes pasados rechazados | ✅ |
| Confirmar reserva | Quote endpoint devuelve desglose completo | ✅ |

### 3.6 Pago y Finalización

| Paso del Diagrama | Implementación | Estado |
|---|---|---|
| Procesar pago | No hay pasarela ni lógica de pago | ❌ |
| ¿Pago aceptado? | No implementado | ❌ |
| Nuevo método de pago (Tarjeta/Bizum/Transf.) | No implementado | ❌ |
| Cancelar proceso | No implementado | ❌ |
| Enviar confirmación (email) | No hay JavaMailSender ni EmailService | ❌ |
| Registrar en sistema (dashboard anual) | No implementado | ❌ |

---

## 4. Requisitos del Briefing vs Implementación

| # | Requisito | Estado | Notas |
|---|---|---|---|
| 1 | Frontend React conectado al back | ❌ | No existe en este repositorio |
| 2 | Frontend responsive | ❌ | No existe frontend aquí |
| 3 | CRUD completo desde frontend | ❌ | No existe frontend |
| 4 | Cloudinary (imágenes hoteles) | ❌ | Sin dependencia en pom.xml; `imageUrl` en modelos sin servicio |
| 5 | Manejo de excepciones | ✅ OK | GlobalExceptionHandler, 6 excepciones custom |
| 6 | DTOs | ✅ OK | ~15 DTOs request/response bien separados |
| 7 | Validaciones | ✅ OK | Jakarta Validation (@NotBlank, @Email, @Future, etc.) |
| 8 | Tests backend | ❌ | Solo test de contexto vacío (`G1AgenciaViajesApplicationTests`) |
| 9 | Tests frontend | ❌ | No existe frontend |
| 10 | Email detallado tras compra | ❌ | Sin dependencia ni servicio |
| 11 | Dashboard dirección (viajes/año, ganancias, top 3) | ❌ | No implementado |
| 12 | Vista viajes en oferta (media pensión y completa) | ✅ | GET /api/travels/sale con precios |
| 13 | Comprar plazas + datos acompañantes | ✅ | Booking con lista de customers |
| 14 | No vender si autobús completo | ❌ | Validación ausente |
| 15 | No vender viajes pasados | ✅ | Validado al crear booking |
| 16 | Menor no viaja sin adulto | ✅ | Validado en BookingServiceImpl |
| 17 | Conductor no conduce 2 buses simultáneamente | ⚠️ | Query existe pero nunca se ejecuta |
| 18 | Usuario puede crear su propio viaje | ❌ | No hay endpoint de "viaje personalizado" |

---

## 5. Código Muerto y Problemas Detectados

| # | Problema | Archivos afectados | Severidad |
|---|---|---|---|
| 1 | `password` en UserRequestDTO nunca se persiste | UserRequestDTO.java | Media |
| 2 | `rol` en User mencionado en README pero no existe en modelo | README.md vs User.java | Media |
| 3 | `DriverOccupiedException` definida pero nunca lanzada | DriverOccupiedException.java, ningún service | Baja |
| 4 | `findOverlappingByDriver()` en repositorio nunca llamada | TripSegmentRepository.java | Alta |
| 5 | `TravelNotFoundException` definida pero nunca lanzada | TravelNotFoundException.java | Baja |
| 6 | `brand` y `year` en BusRequestDTO pero no mapeados | BusRequestDTO.java, BusServiceImpl.java | Media |
| 7 | Inyección inconsistente: mezcla field y constructor injection | BookingController, OfferController, EmployeeController vs otros | Baja |
| 8 | `Bus.available` comentado en entidad | Bus.java | Baja |
| 9 | `Driver.enrollment` comentado en clase | Driver.java, DriverSummaryDTO.java, DriverMapper.java | Baja |
| 10 | README desactualizado (Java 17 dice, 25 usa; Spring Boot 3.2.5 dice, 4.0.6 usa) | README.md vs pom.xml | Media |
| 11 | `application.properties` usa `travel_agency_db`, README dice `agencia_viajes` | application.properties vs README.md | Baja |

---

## 6. Discrepancias README vs Código Real

| README dice | Código real | Impacto |
|---|---|---|
| `password` en User | User model NO tiene password | Confusión |
| `rol` (ADMIN/USER) en User | No existe en modelo | Funcional |
| Java 17 | `java.version=25` | Compilación |
| Spring Boot 3.2.5 | spring-boot-starter-parent 4.0.6 | Compilación |
| Cloudinary configurado | Sin dependencia, sin servicio | Funcional |
| JavaMailSender configurado | Sin dependencia, sin servicio | Funcional |
| `Booking` tiene id (Long) | Booking tiene bookingId (Long) | API |
| UserController GET /activos | ✅ Coincide | - |
| README menciona CustomerBooking | Sí existe como entity separada | - |

---

## 7. Estado de los Sprints (Original — datación incorrecta)

| Sprint | Fecha | Estado |
|---|---|---|
| Sprint 1 | Lunes 4 de mayo | ❌ Vencido |
| Sprint 2 | Viernes 8 de mayo | ❌ Vencido |
| Sprint 3 | Viernes 15 de mayo | ❌ Vencido |
| Sprint Final | Viernes 22 de mayo | ❌ Vencido (ayer) |

---

## 8. Plan de Acción Exhaustivo (Original — 4-5 semanas)

### Prioridades: 🔴 Crítica | 🟡 Alta | 🔵 Media | 🟢 Baja

### Fase 1: Cimientos (Backend)

#### 1.1 Autenticación y Autorización (🔴 Crítica)

| Tarea | Descripción | Archivos a crear/modificar |
|---|---|---|
| 1.1.1 | Agregar dependencia Spring Security al pom.xml | pom.xml |
| 1.1.2 | Crear modelo Rol (enum ADMIN/USER) y agregar campo `rol` a User | Rol.java, User.java |
| 1.1.3 | Agregar campo `password` (hasheado) al modelo User | User.java |
| 1.1.4 | Crear endpoint `POST /api/auth/register` (registro) | AuthController.java, AuthService.java |
| 1.1.5 | Crear endpoint `POST /api/auth/login` que devuelva JWT | AuthController.java, AuthService.java |
| 1.1.6 | Configurar JWT (filtro, utilidades, secret) | JwtUtil.java, JwtAuthFilter.java, SecurityConfig.java |
| 1.1.7 | Configurar SecurityFilterChain con permisos por rol | SecurityConfig.java |
| 1.1.8 | Actualizar README con endpoints de auth | README.md |

#### 1.2 Validaciones de Disponibilidad (🔴 Crítica)

| Tarea | Descripción | Archivos |
|---|---|---|
| 1.2.1 | Al crear booking, verificar capacidad del bus en los TripSegments del viaje | BookingServiceImpl.java |
| 1.2.2 | Agregar método `reducirPlazasBus()` / `liberarPlazasBus()` en BusService | BusService.java, BusServiceImpl.java |
| 1.2.3 | Llamar a `findOverlappingByDriver()` al asignar conductor a TripSegment | TripSegmentService.java |
| 1.2.4 | Lanzar `DriverOccupiedException` cuando corresponda | TripSegmentService.java |

#### 1.3 Dashboard de Dirección (🔴 Crítica)

| Tarea | Descripción | Archivos |
|---|---|---|
| 1.3.1 | Crear DashboardController con endpoints estadísticos | DashboardController.java |
| 1.3.2 | Endpoint: viajes organizados por año | DashboardService.java |
| 1.3.3 | Endpoint: ganancias totales año en curso | DashboardService.java |
| 1.3.4 | Endpoint: top 3 viajes que más recaudan año en curso | DashboardService.java |
| 1.3.5 | Endpoint: reservas por mes (opcional) | DashboardService.java |
| 1.3.6 | Queries de agregación en BookingRepository | BookingRepository.java |

#### 1.4 Email de Confirmación (🟡 Alta)

| Tarea | Descripción | Archivos |
|---|---|---|
| 1.4.1 | Agregar dependencia spring-boot-starter-mail al pom.xml | pom.xml |
| 1.4.2 | Crear EmailService con método sendBookingConfirmation() | EmailService.java |
| 1.4.3 | Configurar JavaMailSender bean | MailConfig.java |
| 1.4.4 | Llamar a EmailService tras crear booking exitosamente | BookingServiceImpl.java |

#### 1.5 Cloudinary (🟡 Alta)

| Tarea | Descripción | Archivos |
|---|---|---|
| 1.5.1 | Agregar dependencia Cloudinary al pom.xml | pom.xml |
| 1.5.2 | Crear CloudinaryConfig con credenciales | CloudinaryConfig.java |
| 1.5.3 | Crear ImageUploadService (subida/eliminación) | ImageUploadService.java |
| 1.5.4 | Endpoint `POST /api/upload` para subir imágenes | UploadController.java |
| 1.5.5 | Integrar subida en creación/edición de Hotel y Driver | HotelServiceImpl.java, DriverServiceImpl.java |

---

### Fase 2: Deuda Técnica (Backend)

#### 2.1 Código Muerto (🔵 Media)

| Tarea | Descripción |
|---|---|
| 2.1.1 | Eliminar campo `password` de UserRequestDTO o implementar su persistencia |
| 2.1.2 | Sincronizar README con el modelo real (eliminar `rol`, `password` de la tabla) |
| 2.1.3 | Mapear `brand` y `year` en BusServiceImpl o eliminarlos de BusRequestDTO |
| 2.1.4 | Unificar estilo de inyección (usar constructor injection en toda la app) |
| 2.1.5 | Limpiar campos comentados (`Bus.available`, `Driver.enrollment`) |

#### 2.2 Tests (🔴 Crítica)

| Tarea | Descripción |
|---|---|
| 2.2.1 | Tests unitarios de BookingPricingService (tarifas y descuentos) |
| 2.2.2 | Tests unitarios de BookingServiceImpl (validaciones, minors, disponibilidad) |
| 2.2.3 | Tests unitarios de UserService (email duplicado, tutor resolution) |
| 2.2.4 | Tests de integración de controllers (cada CRUD) |
| 2.2.5 | Tests de repositorio con @DataJpaTest |

### Fase 3: Frontend (React)

#### 3.1 Setup y Conexión con Back (🔴 Crítica)

| Tarea | Descripción |
|---|---|
| 3.1.1 | Inicializar proyecto React con Vite |
| 3.1.2 | Configurar Axios con URL base del backend |
| 3.1.3 | Configurar interceptors para token JWT |
| 3.1.4 | Configurar React Router (rutas protegidas) |

#### 3.2 Autenticación (🔴 Crítica)

| Tarea | Descripción |
|---|---|
| 3.2.1 | Vista Login (email + password) |
| 3.2.2 | Vista Registro (name, surname, email, password, dni, age) |
| 3.2.3 | AuthContext o store para sesión y rol |
| 3.2.4 | Proteger rutas según rol (Admin vs User) |

#### 3.3 CRUDs desde Front (🔴 Crítica)

| Tarea | Descripción |
|---|---|
| 3.3.1 | CRUD Usuarios: listado, creación, edición, eliminación |
| 3.3.2 | CRUD Hoteles: listado, creación, edición, eliminación + subida imagen |
| 3.3.3 | CRUD Autobuses: listado, creación, edición, eliminación |
| 3.3.4 | CRUD Conductores: listado, creación, edición, eliminación |
| 3.3.5 | CRUD Viajes: listado, creación, edición, eliminación |
| 3.3.6 | CRUD Reservas: listado, detalle, cancelación |

#### 3.4 Flujo de Compra (🔴 Crítica)

| Tarea | Descripción |
|---|---|
| 3.4.1 | Vista "Ver ofertas" con viajes en sale y precios (media pensión y completa) |
| 3.4.2 | Selección de viaje y tipo de pensión |
| 3.4.3 | Selección de número de plazas |
| 3.4.4 | Formulario dinámico acompañantes (nombre, apellido, edad) |
| 3.4.5 | Validación: si menor → campo tutor obligatorio |
| 3.4.6 | Quote preview (GET /api/bookings/quote) con desglose |
| 3.4.7 | Confirmación de reserva (POST /api/bookings) |
| 3.4.8 | Pantalla de éxito con resumen |

#### 3.5 Dashboard Admin (🟡 Alta)

| Tarea | Descripción |
|---|---|
| 3.5.1 | Vista Dashboard con tarjetas de KPIs |
| 3.5.2 | Gráfico viajes por año |
| 3.5.3 | Top 3 viajes más rentables |
| 3.5.4 | Ganancia total año en curso |

#### 3.6 Responsive (🟡 Alta)

| Tarea | Descripción |
|---|---|
| 3.6.1 | Diseño mobile-first con CSS modules o Tailwind |
| 3.6.2 | Menú responsive (hamburger para mobile) |
| 3.6.3 | Tablas adaptables (cards en mobile) |

#### 3.7 Tests Frontend (🔵 Media)

| Tarea | Descripción |
|---|---|
| 3.7.1 | Tests de componentes con React Testing Library |
| 3.7.2 | Tests de flujo de compra (integración) |
| 3.7.3 | Tests de autenticación |

---

### Fase 4: Funcionalidad Extra

#### 4.1 Viaje Personalizado (🟡 Alta)

| Tarea | Descripción |
|---|---|
| 4.1.1 | Endpoint/Formulario para que el usuario cree viaje propio (seleccionar fechas, hotel, origen/destino, bus) |
| 4.1.2 | Validaciones de disponibilidad en viaje custom |

#### 4.2 Procesamiento de Pago (🔵 Media)

| Tarea | Descripción |
|---|---|
| 4.2.1 | Integración con pasarela de pago simulada (o mock) |
| 4.2.2 | Flujo de reintento con nuevo método de pago |
| 4.2.3 | Estados de pago en Booking (PENDING, PAID, FAILED, REFUNDED) |

---

## Resumen de Prioridades (Original)

| Fase | Prioridad | Esfuerzo estimado |
|---|---|---|
| 1.1 Autenticación | 🔴 Crítica | 2-3 días |
| 1.2 Validaciones disponibilidad | 🔴 Crítica | 1 día |
| 1.3 Dashboard | 🔴 Crítica | 2 días |
| 1.4 Email | 🟡 Alta | 1 día |
| 1.5 Cloudinary | 🟡 Alta | 1 día |
| 2.1 Deuda técnica | 🔵 Media | 1 día |
| 2.2 Tests backend | 🔴 Crítica | 3-4 días |
| 3 Frontend completo | 🔴 Crítica | 2-3 semanas |
| 4.1 Viaje personalizado | 🟡 Alta | 1-2 días |
| 4.2 Pago | 🔵 Media | 1-2 días |

**Total estimado**: ~4-5 semanas para completar todo.
