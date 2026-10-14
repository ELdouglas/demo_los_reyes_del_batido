FROM eclipse-temurin:17-jdk-jammy as build
WORKDIR /app

# Copiar los archivos del proyecto
COPY . .

# Dar permisos de ejecución al gradlew y compilar
RUN chmod +x gradlew
RUN ./gradlew bootJar -x test --stacktrace

# Fase de ejecución final
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
