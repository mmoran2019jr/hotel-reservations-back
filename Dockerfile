# --------- Etapa 1: build con Maven ---------
FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests

# --------- Etapa 2: imagen ligera ---------
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar


# --------- Variables de entorno para configuracion en docker ---------
	#--- Conexion base Mongo, jwt y perfil ---
ENV SPRING_PROFILES_ACTIVE=dev
ENV MONGODB_URI=mongodb://mongo:27017/hotel_reservations_dev
ENV JWT_SECRET=super-clave-jwt-larga-y-segura-para-docker-2025
ENV JWT_EXPIRATION_MS=86400000

#Puerto de backend en docker
EXPOSE 8082  

ENTRYPOINT ["java", "-jar", "app.jar"]
