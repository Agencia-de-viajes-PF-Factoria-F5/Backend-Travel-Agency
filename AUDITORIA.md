# Auditoría del Proyecto — g1-agencia-viajes

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

## 7. Estado de los Sprints

Hoy: **Martes 12 de mayo de 2026**

| Sprint | Fecha | Estado |
|---|---|---|
| Sprint 1 | Lunes 4 de mayo | ❌ Vencido |
| Sprint 2 | Viernes 8 de mayo | ❌ Vencido |
| Sprint 3 | Viernes 15 de mayo (este viernes) | ⏳ En curso |
| Sprint Final | Viernes 22 de mayo (próximo viernes) | 📅 Pendiente |

---

## 8. Plan de Acción Exhaustivo

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

## 8. Plan de Acción — Sprint Final (10 días)

### Estrategia

Con **10 días hasta el sprint final (22 mayo)** y parte del equipo posiblemente centrada en el frontend, el plan se divide en dos vías paralelas:

**VÍA A — Backend** (días 1-7): Cerrar toda la lógica pendiente para que la API sea funcional y cumpla los criterios de aceptación.

**VÍA B — Frontend** (días 1-10): Construir React app con lo mínimo indispensable para demostrar el flujo completo.

> **Regla de oro**: Lo que no esté en el sprint final, no entra. Hay que ser quirúrgicos.

### 8.1 Backend — Prioridades (Días 1-7)

#### Día 1: Autenticación (🔴 Crítica)

| Tarea | Archivos | Dependencias |
|---|---|---|
| Agregar Spring Security + JWT al pom.xml | `pom.xml` | Ninguna |
| Crear enum `Role` (ADMIN, USER) + campo `rol` en User | `Role.java`, `User.java` | - |
| Agregar campo `password` hasheado en User | `User.java` | - |
| Endpoint `POST /api/auth/register` | `AuthController.java`, `AuthService.java` | - |
| Endpoint `POST /api/auth/login` → JWT | `AuthController.java`, `AuthService.java` | - |
| SecurityConfig + JwtAuthFilter | `SecurityConfig.java`, `JwtAuthFilter.java`, `JwtUtil.java` | - |
| Seed: crear usuario ADMIN por defecto al arrancar | `DataInitializer.java` o `CommandLineRunner` | - |

#### Día 2: Validaciones de Disponibilidad (🔴 Crítica)

| Tarea | Archivos | Dependencias |
|---|---|---|
| Validar bus disponible al crear booking (sumar plazas ocupadas en TripSegments vs capacidad del bus) | `BookingServiceImpl.java` | Día 1 |
| `BusService.reducirPlazas()` / `liberarPlazas()` | `BusService.java`, `BusServiceImpl.java` | - |
| Llamar `findOverlappingByDriver()` al crear/actualizar TripSegment | `TripSegmentService.java` | - |
| Lanzar `DriverOccupiedException` cuando toque | `TripSegmentService.java` | - |
| **Criterio de aceptación cumplido**: conductor no conduce 2 buses a la vez | - | - |
| **Criterio de aceptación cumplido**: no se vende si bus completo | - | - |

#### Día 3: Dashboard Backend (🔴 Crítica)

| Tarea | Archivos | Dependencias |
|---|---|---|
| Endpoint `GET /api/dashboard/yearly-stats?year=2026` → viajes organizados, ganancias totales | `DashboardController.java`, `DashboardService.java` | BookingRepository |
| Endpoint `GET /api/dashboard/top-travels?year=2026` → top 3 viajes que más recaudan | `DashboardController.java`, `DashboardService.java` | BookingRepository |
| Queries de agregación en BookingRepository (JPQL @Query con GROUP BY, SUM) | `BookingRepository.java` | - |

#### Día 4: Email (🟡 Alta) + Código Muerto (🔵 Media)

| Tarea | Archivos | Dependencias |
|---|---|---|
| spring-boot-starter-mail en pom.xml | `pom.xml` | - |
| EmailService con `sendBookingConfirmation(booking, to)` | `EmailService.java` | - |
| Config JavaMailSender | `MailConfig.java` | - |
| Llamar email al crear booking | `BookingServiceImpl.java` | - |
| Sincronizar README con código real (Java 25, Spring 4.0.6) | `README.md` | - |
| Eliminar código muerto (`password` no persistido, `brand`/`year` sin mapear) | varios | - |

#### Día 5: Cloudinary (🟡 Alta)

| Tarea | Archivos | Dependencias |
|---|---|---|
| Dependencia Cloudinary en pom.xml | `pom.xml` | - |
| CloudinaryConfig + ImageUploadService | `CloudinaryConfig.java`, `ImageUploadService.java` | - |
| Endpoint `POST /api/upload` | `UploadController.java` | - |
| Integrar subida en creación/edición de Hotel y Driver | `HotelServiceImpl.java`, `DriverServiceImpl.java` | - |

#### Días 6-7: Tests Backend (🔴 Crítica)

| Tarea | Archivos | Esfuerzo |
|---|---|---|
| Tests de BookingPricingService (tarifas niño/adulto/pensionista, descuento grupo, oferta) | `BookingPricingServiceTest.java` | 4h |
| Tests de BookingServiceImpl (minors, disponibilidad bus/hotel, fechas) | `BookingServiceImplTest.java` | 4h |
| Tests de AuthService (registro, login, roles) | `AuthServiceTest.java` | 2h |
| Tests de integración: 1 controller core (Bookings, Auth) | `*ControllerTest.java` | 4h |

### 8.2 Frontend React — Prioridades (Días 1-10)

> **Suposición**: el frontend se construye en un repositorio separado o en `g1-agencia-viajes-front/`.

#### Días 1-2: Setup + Auth

| Tarea | Descripción |
|---|---|
| Inicializar React + Vite + React Router | `npm create vite@latest` |
| Configurar Axios con base URL e interceptor JWT | `src/api/axios.js` |
| AuthContext (login, logout, user, token, role) | `src/context/AuthContext.jsx` |
| Vista Login + Registro | `src/pages/Login.jsx`, `Register.jsx` |
| Ruta protegida (si no auth → redirect login) | `src/components/ProtectedRoute.jsx` |
| Navbar con login/logout + rol | `src/components/Navbar.jsx` |

#### Días 3-4: Layout Base + CRUDs Admin

| Tarea | Descripción |
|---|---|
| Layout admin (sidebar) y layout usuario | `src/layouts/` |
| CRUD Usuarios (listar, crear, editar, eliminar) | `src/pages/admin/Users.jsx` |
| CRUD Hoteles (listar, crear, editar, eliminar) | `src/pages/admin/Hotels.jsx` |
| CRUD Autobuses (listar, crear, editar, eliminar) | `src/pages/admin/Buses.jsx` |
| CRUD Conductores (listar, crear, editar, eliminar) | `src/pages/admin/Drivers.jsx` |

#### Días 5-7: Flujo de Compra (CORAZÓN DEL PROYECTO)

| Tarea | Descripción |
|---|---|
| Vista "Viajes en Oferta" con precio media pensión y completa | `src/pages/Ofertas.jsx` |
| Seleccionar viaje, tipo pensión y nº de plazas | `src/pages/SeleccionarViaje.jsx` |
| Formulario acompañantes (nombre, apellido, edad) — dinámico según plazas | `src/components/AcompanantesForm.jsx` |
| Validación: si edad < 18 → campo tutor obligatorio | integrado en formulario |
| Quote preview llamando a `POST /api/bookings/quote` | `src/pages/Resumen.jsx` |
| Confirmar reserva `POST /api/bookings` | `src/pages/Confirmacion.jsx` |
| Pantalla de éxito con resumen del viaje | `src/pages/Exito.jsx` |

#### Día 8: Dashboard Admin

| Tarea | Descripción |
|---|---|
| Vista Dashboard con tarjetas (viajes año, ganancias, top 3) | `src/pages/admin/Dashboard.jsx` |
| Llamadas a endpoints de dashboard | `src/api/dashboard.js` |
| Gráficos simples (opcional: Chart.js o datos en tabla) | - |

#### Día 9: Detalles + Responsive

| Tarea | Descripción |
|---|---|
| Subida de imágenes a Cloudinary en formulario Hotel/Driver | integrado en CRUDs |
| Responsive básico (media queries, menú hamburguesa) | `src/App.css` o Tailwind |
| Estados de carga, error y vacío en todas las vistas | `src/components/Spinner.jsx`, `ErrorAlert.jsx` |

#### Día 10: Tests Frontend + Polishing

| Tarea | Descripción |
|---|---|
| Tests de componentes clave (Login, Ofertas, AcompanantesForm) | `*.test.jsx` con React Testing Library |
| Test de flujo de compra (integración) | `src/__tests__/compraFlow.test.jsx` |
| Últimas correcciones, build, verificación | `npm run build` |

### 8.3 Lo que NO entra en el Sprint Final

| Funcionalidad | Motivo |
|---|---|
| Viaje personalizado (usuario crea su propio viaje) | Requiere mucho diseño y validación extra |
| Pasarela de pago real | Se puede simular (pago automáticamente aceptado) |
| Tests exhaustivos de todos los CRUDs | Solo se testea lo crítico (auth, booking, pricing) |
| CustomerBooking entity | Ya existe, pero si requiere cambios complejos se deja como está |
| Refactor de inyección de dependencias | Mejora estética, no funcional |

### 8.4 Dependencias y Riesgos

| Riesgo | Impacto | Mitigación |
|---|---|---|
| Frontend desde cero en 10 días | Alto | Priorizar flujo de compra sobre CRUDs admin |
| Spring Security + JWT configuración | Medio | Usar plantilla/base de proyecto anterior |
| Cloudinary requiere cuenta | Bajo | Crear cuenta gratuita (5min) |
| Java 25 + Spring Boot 4.0.6 muy nuevos | Medio | Si hay incompatibilidades, bajar a Java 17 + Spring Boot 3.2.5 como dice README |
| Equipo pequeño | Alto | Dividir back/front en paralelo desde día 1 |

### 8.5 Roadmap Visual (10 días)

```
Semana 1 (12-16 mayo) — Sprint 3
┌─────────────────────────────────────────────────────────┐
│ LUN 12  │ MAR 13  │ MIÉ 14  │ JUE 15  │ VIE 16       │
├─────────┼─────────┼─────────┼─────────┼───────────────┤
│ BACK:   │ BACK:   │ BACK:   │ BACK:   │ BACK:         │
│ Auth    │ Dispon. │ Dashbrd │ Email   │ Cloudinary    │
│ FRONT:  │ FRONT:  │ FRONT:  │ FRONT:  │ FRONT:        │
│ Setup   │ Setup + │ CRUDs   │ CRUDs   │ Flujo Compra  │
│ + Auth  │ Auth UI │ Admin   │ Admin   │ (inicio)      │
└─────────┴─────────┴─────────┴─────────┴───────────────┘

Semana 2 (19-22 mayo) — Sprint Final
┌───────────────────────────────────────────────────────┐
│ LUN 19    │ MAR 20    │ MIÉ 21    │ JUE 22           │
├───────────┼───────────┼───────────┼───────────────────┤
│ FRONT:    │ FRONT:    │ FRONT:    │ FRONT:            │
│ Flujo     │ Dashboard │ Detalles  │ Tests finales     │
│ Compra    │ + Imgs    │ + Resp.   │ BACK:             │
│ (terminar)│           │           │ Tests (días 6-7)  │
│ BACK:     │           │           │                   │
│ Tests     │           │           │ 🚀 ENTREGA        │
└───────────┴───────────┴───────────┴───────────────────┘
```

### 8.6 Criterios de Aceptación — Checklist Final

| # | Criterio | Estado esperado al 22 mayo |
|---|---|---|
| 1 | Cliente compra viaje para varias personas | ✅ Flujo completo front+back |
| 2 | No vender si autobús completo | ✅ Validado en backend |
| 3 | No vender viajes pasados | ✅ Ya implementado |
| 4 | Menor no viaja sin adulto | ✅ Ya implementado |
| 5 | Conductor no conduce 2 buses simultáneamente | ✅ Validado en backend |
| 6 | Dashboard dirección (viajes/año, ganancias, top 3) | ✅ Endpoints + vista admin |
| 7 | Email de confirmación tras compra | ✅ Envío real o log |
| 8 | Frontend responsive | ✅ Media queries básicas |
| 9 | Tests (front + back) | ✅ Mínimo: pricing, auth, flujo compra |
