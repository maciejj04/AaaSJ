FROM openjdk:8
ADD ./target/AaaSJ-0.0.1-SNAPSHOT.jar
EXPOSE 8080
CMD ["java", "-jar", "AaaSJ-0.0.1-SNAPSHOT.jar", "-Dspring.profiles.active=prod"]