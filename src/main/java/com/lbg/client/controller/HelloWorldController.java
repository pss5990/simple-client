package com.lbg.client.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

/**
 * "/hello" -> Sample Get End point, which when called will call another Micro-service(MS) and
 * return whatever the response is
 * 
 * But now this has hystrix enabled. So the fallback method will be called in case of an exception from the sayHelloWorld method.
 * Also note that the hystrix configuration below is a simple one, we can tune it to our needs.
 */
@RestController
@RequestMapping(value = "/dummy")
public class HelloWorldController {

  @Value("${test.server.url}")
  private String serverURL;

  private Logger logger = LogManager.getLogger();

  @RequestMapping(value = "/hello", method = RequestMethod.GET)
  @HystrixCommand(fallbackMethod = "fallbackMethod")
  public ResponseEntity<String> sayHelloWithFallback() {
    RestTemplate restTemplate = new RestTemplate();
    logger.info("Server Url->{}", () -> serverURL);
    ResponseEntity<String> re = restTemplate.getForEntity(serverURL, String.class);
    return new ResponseEntity<String>(re.getBody(), HttpStatus.OK);
  }
  
  @RequestMapping(value = "/hello1", method = RequestMethod.GET)
  @HystrixCommand(fallbackMethod = "fallbackMethod")
  public ResponseEntity<String> sayHello() {
    return new ResponseEntity<String>("Test message from hello1", HttpStatus.OK);
  }

  public ResponseEntity<String> fallbackMethod() {
    return new ResponseEntity<String>("I am a fallback message using hstrix", HttpStatus.OK);
  }
}
