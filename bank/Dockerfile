FROM openjdk:11-oracle
EXPOSE 8081
ARG JAR_FILE=build/libs/bank-0.0.4-SNAPSHOT.jar
ADD ${JAR_FILE} docker-springboot.jar
ENTRYPOINT ["java", "-jar", "/docker-springboot.jar"]
