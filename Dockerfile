FROM openjdk:8-alpine
COPY ./target/entersekt-dir-1.0-SNAPSHOT-jar-with-dependencies.jar /opt/bin/entersekt-dir.jar
EXPOSE 8080
CMD ["java", "-jar", "/opt/bin/entersekt-dir.jar"]
