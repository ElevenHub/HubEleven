FROM eclipse-temurin:17-jdk
ARG SERVICE_NAME

ENV TZ=Asia/Seoul
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN mkdir /app

ARG JAR_FILE=${SERVICE_NAME}/build/libs/*.jar
COPY ${JAR_FILE} /app/app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]