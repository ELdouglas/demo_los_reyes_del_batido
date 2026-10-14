FROM eclipse-temurin:17-jdk-jammy as build
WORKDIR /app


COPY . .


RUN chmod +x gradlew
RUN ./gradlew bootJar -x test


FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
