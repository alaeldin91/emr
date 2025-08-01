FROM openjdk:21-oracle

WORKDIR /app

COPY target/service-template.jar service-template.jar

EXPOSE 8081

CMD ["java", "-jar", "service-template.jar"]
