FROM eclipse-temurin:17.0.9_9-jdk
ADD . /src
WORKDIR /src
RUN chmod +x mvnw
RUN ./mvnw package -DskipTests
EXPOSE 8080
COPY target/*.jar /app.jar
ENTRYPOINT ["java","-jar","/app.jar"]