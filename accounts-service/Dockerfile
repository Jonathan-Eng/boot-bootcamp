FROM openjdk:8
COPY ./build/libs/ /usr/bootcamp/
COPY ./build/resources/ /usr/bootcamp/resources/
WORKDIR /usr/bootcamp/

# Renaming the JAR file
RUN mv accounts-service-1.0-SNAPSHOT-all.jar accounts-service.jar

CMD ["java", "-jar", "accounts-service.jar"]