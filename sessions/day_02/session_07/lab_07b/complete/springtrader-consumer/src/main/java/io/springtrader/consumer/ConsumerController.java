package io.springtrader.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsumerController {
    
    @Autowired
    ProducerClient client;
    
    @RequestMapping(value="/", produces="application/json")
    public String consume() {

        
        ProducerResponse response = client.getValue();
        return String.format("{\"server port\":%d, \"value\":%d}", response.getServerPort(), response.getValue());

    }
   
}
