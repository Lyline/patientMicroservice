FROM openjdk:11-jre-slim-buster
ARG JAR_FILE=build/libs/patientMicroservice-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} patientMicroservice-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-Dspring.profiles.active=prod","-jar","patientMicroservice-0.0.1-SNAPSHOT.jar"]
EXPOSE 8081