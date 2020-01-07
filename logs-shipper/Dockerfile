FROM openjdk:8
COPY ./build/libs/ /usr/bootcamp/
COPY ./build/resources/ /usr/bootcamp/resources/
EXPOSE 8000
WORKDIR /usr/bootcamp/
RUN mv logs-shipper-1.0-SNAPSHOT-all.jar shipper.jar
CMD ["java", "-Dlog4j.configurationFile=/usr/bootcamp/resources/main/log4j2.xml", "-jar", "shipper.jar"]