# Usa una imagen base de Maven con Java 17 para construir
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Setea directorio de trabajo
WORKDIR /app

# Copia el archivo pom.xml y descarga dependencias
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia el resto del proyecto
COPY . .

# Empaqueta la app (genera el .jar)
RUN mvn clean package -DskipTests

# Usa una imagen ligera solo con Java para correr el jar
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copia el jar desde la etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Exponer puerto dinámico (Render usará $PORT)
ENV PORT 8080
EXPOSE 8080

# Comando de arranque
CMD ["java", "-jar", "app.jar"]
