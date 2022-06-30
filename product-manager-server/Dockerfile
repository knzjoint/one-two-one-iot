FROM openjdk:11.0.14.1-jdk
ADD server-0.0.1-SNAPSHOT.jar /root/product-server.jar
EXPOSE 9200
ENTRYPOINT ["java","-jar","/root/product-server.jar","--spring.profiles.active=docker"]
