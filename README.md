🏨 Hotel Reservations API – Backend
Backend del sistema de reservas de hotel construido con Spring Boot 3, MongoDB y JWT. Provee endpoints para gestionar usuarios, habitaciones, reservas y procesos de checkout.

🚀 Tecnologías
Java 17
Spring Boot 3
Spring Web
Spring Security (JWT)
Spring Data MongoDB
Maven
Docker / Docker Compose

📌 Funcionalidades principales

🔐 Autenticación
Registro y login de usuarios
Generación y validación de JWT
Protección de rutas con SecurityFilterChain

🏨 Habitaciones
Listado paginado
Filtros por tipo, precio, capacidad y disponibilidad
Detalles de habitación
Manejo de imágenes mediante URLs simuladas

📅 Reservas
Crear reserva validando disponibilidad
Consultar reservas por usuario
Modificar o cancelar reserva
Cálculo automático de noches y costo total

✔️ Checkout
Finalización de reserva
Registro en historial
Validación de conflictos de fechas

🛠 Configuración
El backend utiliza perfiles de Spring:

dev (por defecto)
Variables de entorno para MongoDB y JWT:
spring:
  data:
    mongodb:
      uri: ${MONGODB_URI:mongodb://localhost:27017/hotel_reservations_dev}


## 🛠 Requisitos de sistema
Java 17

Maven 3.9+

MongoDB local (27017)

##Iniciar la API
mvn spring-boot:run

La API estará disponible en:

http://localhost:8082/api

🐳 Ejecutar con Docker
Construir imagen
docker compose build

Levantar servicios
docker compose up -d


Servicios expuestos:

API: http://localhost:8081/api

MongoDB: mongodb://localhost:27017

## endpoints principales

POST /api/auth/register

POST /api/auth/login

GET /api/rooms

GET /api/rooms/{id}

POST /api/reservations

PUT /api/reservations/{id}

DELETE /api/reservations/{id}

POST /api/checkout/{id}
