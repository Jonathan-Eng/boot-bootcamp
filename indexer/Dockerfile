FROM openjdk:8
COPY ./build/libs/ /usr/bootcamp/
COPY ./build/resources/ /usr/bootcamp/resources/
WORKDIR /usr/bootcamp/

# Renaming the JAR file
RUN mv indexer-1.0-SNAPSHOT-all.jar indexer.jar

CMD ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5115", "-jar", "indexer.jar"]