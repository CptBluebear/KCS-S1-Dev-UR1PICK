FROM gradle:7.5.1-jdk11-alpine as BUILD_STAGE
WORKDIR /app
ADD ./ ./
RUN gradle clean build --no-daemon

FROM eclipse-temurin:11.0.16_8-jre-alpine
WORKDIR /app
COPY --from=BUILD_STAGE /app/build/libs/Kafe-Preview.jar .
EXPOSE 8080
CMD ["java", "-jar", "Kafe-Preview.jar", "--spring.config.location=/configuration/application.yml"]
