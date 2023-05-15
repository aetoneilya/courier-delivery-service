FROM openjdk:17.0.1-jdk-slim

WORKDIR /opt/app

COPY *.gradle ./
COPY src ./src/
COPY gradlew .
COPY gradle ./gradle/

RUN chmod +x gradlew
RUN sed -i 's/\r$//' gradlew
RUN bash gradlew bootJar

ENTRYPOINT ["java","-jar","build/libs/yandex-lavka-0.0.1-SNAPSHOT.jar"]
