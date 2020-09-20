package com.lbg.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;

/**
 * If you run this class within your IDE, my doing a right click and saying run as java application, the application will be deployed in an 
 * embedded tomcat/jetty server (depending what you have provided on your classpath). This is known as deploying an application in an embedded server.
 * 
 * If you would like to achieve the same thing (deploying in an embedded server)using the command line. You can add the spring-boot-maven-plugin to the
 * pom's build section and during the build time, an embedded server will be packaged into the war/jar and then you can run it by doing
 * java -jar <jar file name> OR you can also do a mvn spring-boot:run both will start your application in an embdded server.
 * 
 */

@SpringBootApplication
@EnableCircuitBreaker
public class MainApplication {

    public static void main(final String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

}
