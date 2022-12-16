FROM eclipse-temurin:11-jre-alpine
ADD ./nanumfont /usr/share/fonts/nanumfont
RUN apk update && apk add --no-cache chromium chromium-chromedriver xvfb-run && fc-cache -f -v
ADD ./selenium-server-4.6.0.jar ./selenium-server-4.6.0.jar
ADD ./run.sh ./run.sh
RUN chmod a+x ./run.sh
EXPOSE 4444
ENTRYPOINT ["./run.sh"]
