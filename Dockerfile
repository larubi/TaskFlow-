# Usa una imagen base de Java optimizada para Spring Boot
FROM eclipse-temurin:22-jdk-jammy AS build

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia los archivos de construcción de Gradle y las fuentes
# Esto optimiza el caché de Docker para reconstrucciones más rápidas
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src

# Dale permisos de ejecución al wrapper de Gradle
RUN chmod +x ./gradlew

# Construye el proyecto Spring Boot y genera el JAR ejecutable
# El flag --no-daemon asegura que Gradle se ejecute como un proceso en primer plano
RUN ./gradlew bootJar --no-daemon

# --- Segunda etapa: Imagen final ligera para la aplicación ---
FROM eclipse-temurin:22-jre-jammy

# Establece el directorio de trabajo
WORKDIR /app

# Expone el puerto que usa tu aplicación Spring Boot (por defecto 8080)
EXPOSE 8080

# Copia el JAR generado de la etapa de construcción a la imagen final
COPY --from=build /app/build/libs/*.jar app.jar

# Define el comando para ejecutar la aplicación cuando se inicie el contenedor
ENTRYPOINT ["java", "-jar", "app.jar"]