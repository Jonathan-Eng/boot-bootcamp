FROM openjdk:8
COPY ./boot-bootcamp/build/libs/ /usr/bootcamp/
EXPOSE 8500
WORKDIR /usr/bootcamp/
CMD ["java", "-jar", "untitled-all-1.0-SNAPSHOT.jar"]
