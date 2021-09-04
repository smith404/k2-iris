FROM openjdk:14-jdk-alpine
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
ARG JAR_FILE=target/*.jar
ARG NLP_DATA=home/nlp-data-root/*
COPY ${JAR_FILE} app.jar
COPY ${NLP_DATA} /home/nlp-data-root/
RUN ls -l /home/nlp-data-root
ENTRYPOINT ["java","-jar","/app.jar"]