package io.springtrader.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ConsumerController {
    
    @Autowired
    private RestTemplate restTemplate;
    
    @RequestMapping(value="/", produces="application/json")
    public String consume() {

        
        ProducerResponse response = restTemplate.getForObject("http://producer", ProducerResponse.class);
        return String.format("{\"server port\":%d, \"value\":%d}", response.getServerPort(), response.getValue());

    }
   
}
