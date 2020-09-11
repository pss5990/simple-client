FROM openjdk:8

COPY target/test-client-1.0.war /usr/src/app/app.war

WORKDIR /usr/src/app

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.war"]