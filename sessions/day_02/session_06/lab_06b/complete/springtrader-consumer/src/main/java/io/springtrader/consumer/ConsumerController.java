package io.springtrader.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ConsumerController {



    @Autowired
    RestTemplate restTemplate;

    @RequestMapping(value = "/", produces = "application/json")
    String consume() {
//        InstanceInfo instance = discoveryClient.getNextServerFromEureka("PRODUCER", false);

//        RestTemplate restTemplate = new RestTemplate();
        ProducerResponse response = restTemplate.getForObject("http://producer", ProducerResponse.class);

        return String.format("{\"value\":%d}", response.getValue());
    }

}
