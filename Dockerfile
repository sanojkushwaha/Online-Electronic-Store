FROM eclipse-temurin:25-jdk

WORKDIR /app

COPY target/OnlineElectronicStore-*.jar app.jar

EXPOSE 9090

ENTRYPOINT ["java", "-jar", "app.jar"]