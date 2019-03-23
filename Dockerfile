FROM openjdk:8u191-jre-alpine3.9

# installs unrar
RUN apk add --no-cache unrar
RUN mkdir -p /files
WORKDIR /files

ARG finalName
COPY target/${finalName} /app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

