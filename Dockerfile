FROM openjdk:17
ARG JAR_FILE=target/parser-0.0.1-SNAPSHOT.jar
WORKDIR /opt/app
COPY ${JAR_FILE} parser.jar
ENTRYPOINT ["java","-jar","parser.jar"]