FROM openjdk:8
COPY ./logs-shipper/build/libs/ /usr/bootcamp/
COPY ./logs-shipper/src/main/resources/server.config /usr/bootcamp/resources/server.config
EXPOSE 8500
WORKDIR /usr/bootcamp/
CMD ["java", "-jar", "logs-shipper-all-1.0-SNAPSHOT.jar"]
