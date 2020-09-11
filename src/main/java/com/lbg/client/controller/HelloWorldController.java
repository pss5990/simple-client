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

/**
 * "/hello" -> Sample Get End point, which when called will call another Micro-service(MS) and return whatever the response is
 */

@RestController
@RequestMapping(value = "/dummy")
public class HelloWorldController {

  @Value("${test.server.url}")
  private String serverURL;

  private Logger logger = LogManager.getLogger();

  @RequestMapping(value = "/hello", method = RequestMethod.GET)
  public ResponseEntity<String> sayHelloWorld() {
    RestTemplate restTemplate = new RestTemplate();
    logger.info("Server Url->{}", () -> serverURL);
    ResponseEntity<String> re = restTemplate.getForEntity(serverURL, String.class);
    return new ResponseEntity<String>(re.getBody(), HttpStatus.OK);
  }
}
