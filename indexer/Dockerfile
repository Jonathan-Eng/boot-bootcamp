FROM openjdk:8
COPY ./build/libs/ /usr/bootcamp/
WORKDIR /usr/bootcamp/

# Renaming the JAR file
RUN mv indexer-1.0-SNAPSHOT-all.jar indexer.jar

CMD ["java", "-jar", "indexer.jar"]