FROM openjdk:17
WORKDIR /app
COPY target/telerandomizer-1.0-SNAPSHOT.jar /app
EXPOSE 80
RUN ["mkdir", "-p", "/app/csv"]
ENTRYPOINT ["java", "-jar", "/app/telerandomizer-1.0-SNAPSHOT.jar"]