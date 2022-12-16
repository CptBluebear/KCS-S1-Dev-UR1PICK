FROM gradle:7.5.1-jdk11-alpine as BUILD_STAGE
WORKDIR /app
ADD ./ ./
RUN gradle clean build --no-daemon

FROM eclipse-temurin:11.0.16_8-jre-alpine
RUN apk --no-cache add tzdata && \
	cp /usr/share/zoneinfo/Asia/Seoul /etc/localtime && \
	echo "Asia/Seoul" > /etc/timezone \
	apk del tzdata
WORKDIR /app
COPY --from=BUILD_STAGE /app/build/libs/Kafe-URL-Redirect.jar .
EXPOSE 8080
CMD ["java", "-jar", "Kafe-URL-Redirect.jar", "--spring.config.location=/configuration/application.yml"]
