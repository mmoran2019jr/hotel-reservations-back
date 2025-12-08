## 🏨 Hotel Reservations API – Backend
Backend del sistema de reservas de hotel construido con Spring Boot 3, MongoDB y JWT. Provee endpoints para gestionar usuarios, habitaciones, reservas y procesos de checkout.

🚀 Tecnologías
Java 17
Spring Boot 3
Spring Web
Spring Security (JWT)
Spring Data MongoDB
Maven
Docker / Docker Compose
MongoDb - Gestor de base de datos

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

## Iniciar la API
mvn spring-boot:run

La API estará disponible en:

http://localhost:8082/api

## 🐳 Dockerfile — Backend (Spring Boot + Java 17)

Este Dockerfile utiliza una construcción multi-etapa para generar una imagen optimizada del backend.
Primero compila el proyecto con Maven y luego crea una imagen final ligera usando JRE Alpine.

🔧 Etapa 1 — Build con Maven (build)
FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests

Etapa 2 — Imagen ligera para producción
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar


⚙️ Variables de entorno (configuración en Docker)
ENV SPRING_PROFILES_ACTIVE=dev
ENV MONGODB_URI=mongodb://mongo:27017/hotel_reservations_dev
ENV JWT_SECRET=super-clave-jwt-larga-y-segura-para-docker-2025
ENV JWT_EXPIRATION_MS=86400000

🌐 Exposición de puerto
EXPOSE 8082

## Endpoints principales
POST http://localhost:8082/api/rooms - Crear habitacion

POST http://localhost:8082/api/reservations - Realizar una reserva

POST http://localhost:8082/api/auth/register - Registro de usuario

PUT http://localhost:8082/api/reservations/69362044b0ac4e605b0c9449 - Actualizar una reserva

(Revisar collection en postman)


# 🚀 Despliegue en Docker (MongoDB + API Spring Boot + Front Angular)
Este docker-compose.yml levanta tres servicios:

mongo → Base de datos MongoDB

api → Backend Java Spring Boot (puerto 8082)

frontend → Frontend Angular servido con Nginx (puerto 4200)

## - Importante! se debe cambiar las rutas de los proyectos api y front por las rutas donde guarden los proyectos
C:/Users/mmora/OneDrive/Documents/backend_javaspring/hotel-reservations-api -> ruta del api
C:/Users/mmora/OneDrive/Documents/HotelApplication/hotel-reservations-front/hotel-front - ruta del front


## 1️ Requisitos previos

Asegúrate de tener instalado:

Docker Desktop

[Docker Compose](viene integrado en Docker Desktop)

## 2️ Ubicación del archivo docker-compose.yml

Guarda el archivo docker-compose.yml en alguna carpeta, por ejemplo:
C:\Users\mmora\OneDrive\Documents\HotelApplication\deploy\docker-compose.yml

Verifica que las rutas de context apunten correctamente a tus proyectos:

Backend:
C:/Users/mmora/OneDrive/Documents/backend_javaspring/hotel-reservations-api

Frontend:
C:/Users/mmora/OneDrive/Documents/HotelApplication/hotel-reservations-front/hotel-front

Si cambias de lugar los proyectos, actualiza las rutas en el docker-compose.yml.

## 3 Construir los empaquetados y despliegue de contenedor en docker
docker compose build - empaquetados y despliegue

docker compose up -d - levantar servicios

## Collection postman
Se adjunta collection de postman HOTEL_DEV.postman_collection.json.

