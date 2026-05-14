# Diagnóstico de Seguridad — `g1-agencia-viajes`

## 1. Stack Tecnológico

| Componente | Tecnología |
|------------|-----------|
| Framework | Spring Boot 4.0.6 (Java 25) |
| JWT | `com.auth0:java-jwt:4.4.0` |
| Hash de passwords | `org.mindrot:jbcrypt:0.4` (BCrypt) |
| Base de datos | MySQL con JPA/Hibernate |
| API Docs | Springdoc OpenAPI (`/swagger-ui.html`, `/api-docs`) |

---

## 2. Arquitectura de Seguridad (3 capas)

```
Cliente → [CORS Config] → [JwtFilter] → [Controller]
                              │
                    ┌─────────┴─────────┐
                    │  Sin token → 401  │
                    │  Token inválido → 401 │
                    │  Token válido → verificar rol │
                    └─────────────────────┘
```

### 2.1 Ficheros de Seguridad

| Fichero | Propósito |
|---------|-----------|
| `security/JwtUtil.java` | Generación de tokens JWT |
| `security/JwtFilter.java` | Filtro que intercepta todas las peticiones `/api/*` |
| `security/SecurityConfig.java` | Registra el filtro en el pipeline de Spring |
| `config/CorsConfig.java` | Configuración CORS (orígenes permitidos) |
| `model/Role.java` | Enum con los 3 roles: `VIEWER`, `EDITOR`, `ADMIN` |
| `model/Employee.java` | Entidad empleado (contiene `password` y `role`) |
| `controller/AuthenticationController.java` | Endpoint de login |
| `dto/LoginRequest.java` | DTO de login (`id` + `password`) |
| `dto/LoginResponse.java` | DTO de respuesta (`token` + datos + `role`) |

---

## 3. Flujo de Autenticación

```
POST /api/authentication/login
Body: { "id": 1, "password": "123456" }
```

```
1. EmployeeRepository.findById(id)
2. BCrypt.checkpw(password_plano, hash_BD)
3. Si coincide → JwtUtil.crearToken(name, id, role)
   Payload del JWT:
   {
     "sub": "Carlos Pérez",
     "id": 1,
     "role": "ADMIN",
     "iss": "agencia-viajes"
   }
4. Responde: { token: "eyJ...", employeeId: 1, name: "...", role: "ADMIN" }
```

**Tokens sin expiración** — no tienen fecha de caducidad.

---

## 4. Control de Acceso por Rol (JwtFilter)

El filtro intercepta TODAS las rutas `/api/*`. Solo `POST /api/authentication/login` está whitelisted (no requiere token).

| Rol | GET (lectura) | POST/PUT/DELETE (modificar) | POST/PUT/DELETE en `/api/employees` |
|-----|:---:|:---:|:---:|
| **VIEWER** | ✅ Permitido | ❌ 403 Forbidden | ❌ 403 Forbidden |
| **EDITOR** | ✅ Permitido | ✅ Permitido | ❌ 403 Forbidden |
| **ADMIN** | ✅ Permitido | ✅ Permitido | ✅ Permitido |

**Respuestas HTTP:**
- `401 Unauthorized` — Token ausente, inválido o expirado
- `403 Forbidden` — Token válido pero el rol no tiene permisos para esa acción

---

## 5. Endpoints Protegidos

| Controller | Endpoints | VIEWER | EDITOR | ADMIN |
|-----------|-----------|:------:|:------:|:-----:|
| **AuthenticationController** | `POST /api/authentication/login` | ✅ | ✅ | ✅ |
| **UserController** | `GET /api/users`, `GET /api/users/{id}`, `GET /api/users/activos` | ✅ | ✅ | ✅ |
| | `POST /api/users`, `PUT /api/users/{id}`, `DELETE /api/users/{id}` | ❌ | ✅ | ✅ |
| **BookingController** | `GET /api/bookings`, `GET /api/bookings/{id}` | ✅ | ✅ | ✅ |
| | `POST /api/bookings`, `POST /api/bookings/quote`, `PUT /api/bookings/{id}`, `DELETE /api/bookings/{id}` | ❌ | ✅ | ✅ |
| **TravelController** | `GET /api/travels`, `GET /api/travels/{id}`, `/available`, `/sale` | ✅ | ✅ | ✅ |
| | `POST /api/travels`, `PUT /api/travels/{id}`, `DELETE /api/travels/{id}` | ❌ | ✅ | ✅ |
| **HotelController** | `GET /api/hotels`, `GET /api/hotels/{id}`, `/activos`, `/disponibles` | ✅ | ✅ | ✅ |
| | `POST /api/hotels`, `PUT /api/hotels/{id}`, `DELETE /api/hotels/{id}` | ❌ | ✅ | ✅ |
| **OfferController** | `GET /api/offers`, `GET /api/offers/{id}` | ✅ | ✅ | ✅ |
| | `POST /api/offers`, `PUT /api/offers/{id}`, `DELETE /api/offers/{id}` | ❌ | ✅ | ✅ |
| **DriverController** | `GET /api/drivers`, `GET /api/drivers/{id}`, `/activos` | ✅ | ✅ | ✅ |
| | `POST /api/drivers`, `PUT /api/drivers/{id}`, `DELETE /api/drivers/{id}` | ❌ | ✅ | ✅ |
| **BusController** | `GET /api/buses`, `GET /api/buses/{id}` | ✅ | ✅ | ✅ |
| | `POST /api/buses`, `PUT /api/buses/{id}`, `DELETE /api/buses/{id}` | ❌ | ✅ | ✅ |
| **TripSegmentController** | `GET /api/trip-segments`, `GET /api/trip-segments/{id}` | ✅ | ✅ | ✅ |
| | `POST /api/trip-segments`, `PUT /api/trip-segments/{id}`, `DELETE /api/trip-segments/{id}` | ❌ | ✅ | ✅ |
| **EmployeeController** | `GET /api/employees`, `GET /api/employees/{id}` | ✅ | ✅ | ✅ |
| | `POST /api/employees`, `PUT /api/employees/{id}`, `DELETE /api/employees/{id}` | ❌ | ❌ | ✅ |

---

## 6. Empleados Seed (data.sql)

| Empleado | ID | Rol | Password |
|----------|:--:|:---:|:--------:|
| Carlos Pérez | 1 | ADMIN | `123456` (BCrypt) |
| Ana Sánchez | 2 | EDITOR | `123456` (BCrypt) |
| Sofía Oliveira | 3 | VIEWER | `123456` (BCrypt) |
| David Thimotheo | 4 | VIEWER | `123456` (BCrypt) |

---

## 7. CORS

```java
.allowedOrigins("http://localhost:5173", ..., "http://localhost:5177")
.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
.allowedHeaders("*")
.allowCredentials(true)
```

Permite peticiones desde `localhost:5173` a `localhost:5177`.

---

## 8. Vulnerabilidades Conocidas

| # | Severidad | Problema |
|--|-----------|----------|
| 1 | 🔴 **Crítica** | **Secret key hardcodeada** (`"your_secret_password"`) en `JwtUtil.java` y `JwtFilter.java` |
| 2 | 🔴 **Crítica** | **Tokens sin expiración** — no tienen `.withExpiresAt()`, valen para siempre |
| 3 | 🔴 **Crítica** | **Passwords en texto plano en `data.sql`** — el seed inserta `'123456'` que luego BCrypt hashea (pero si se re-ejecuta, se re-hashea) |
| 4 | 🟠 **Alta** | **Login usa ID numérico** (clave primaria) en vez de email/username — es enumerable |
| 5 | 🟠 **Alta** | **`GET /api/employees` devuelve los hashes de password** — no hay `@JsonIgnore` en `password` |
| 6 | 🟠 **Alta** | **Sin refresh token ni revocación** — no se puede invalidar un token robado |
| 7 | 🟡 **Media** | **Sin HTTPS** — las contraseñas viajan en texto plano |
| 8 | 🟡 **Media** | **Credenciales BD en texto plano** (`root`/`root` en `application.properties`) |
| 9 | 🟡 **Media** | **Swagger UI expuesto** en producción (`/swagger-ui.html`, `/api-docs`) |
| 10 | 🟢 **Baja** | **Sin rate limiting** en el endpoint de login — posible brute force |

---

## 9. Rama `dev` vs Rama `feature/security`

| Aspecto | `dev` (compañeros) | `feature/security` |
|---------|:------------------:|:------------------:|
| Login requiere token | ❌ Todas las APIs públicas | ✅ Solo login whitelisted |
| Control de roles | ✅ Implementado (pero no se ejecuta porque no hay auth) | ✅ Activo |
| Seed data | 4 empleados con rol | 4 empleados con rol |
| JwtFilter whitelist | Todas las rutas `/api/*` | Solo `/api/authentication/login` |

La rama `dev` tiene los roles implementados en el código pero **no están activos** porque el filtro permite todo sin token. La rama `feature/security` es idéntica excepto que el JwtFilter **sí requiere autenticación**.

---

## 10. Diagrama de Flujo Completo

```
┌─────────────────────────────────────────────────────────┐
│                    CLIENTE                              │
│  (Frontend React en localhost:5173)                     │
└────────────────────┬────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────┐
│                  CORS Config                             │
│  ¿Origen permitido? → localhost:5173-5177               │
└────────────────────┬────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────┐
│                  JwtFilter                               │
│                                                         │
│  ┌─ ¿Es /api/authentication/login? ─→ Pasar sin token ─┤
│  │                                                      │
│  └─ ¿Tiene header "Authorization: Bearer ..."?          │
│       │                                                 │
│       ├─ NO → 401 "Token ausente o inválido"            │
│       │                                                 │
│       └─ SÍ → Verificar JWT                             │
│                │                                        │
│                ├─ Inválido → 401 "Token inválido"       │
│                │                                        │
│                └─ Válido → Extraer rol del JWT          │
│                            │                            │
│               ┌────────────┼────────────┐               │
│               ▼            ▼            ▼               │
│           VIEWER        EDITOR       ADMIN              │
│               │            │            │               │
│         ¿GET? ── SÍ → ✅  │            │               │
│               │            │            │               │
│               └─ NO → 403  │            │               │
│                            │            │               │
│                   ¿ruta /api/            │               │
│                    employees?            │               │
│                       │                  │               │
│                    ┌──┴──┐               │               │
│                    SÍ    NO              │               │
│                    ❌     ✅              │               │
│                   403                     │              │
│                                           │              │
│                                 ┌─────────┘              │
│                                 ▼                        │
│                    → Controller (operación permitida)    │
└─────────────────────────────────────────────────────────┘
```
