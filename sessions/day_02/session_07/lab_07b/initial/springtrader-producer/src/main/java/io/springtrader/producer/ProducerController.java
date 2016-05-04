package io.springtrader.producer;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProducerController {
    
    @Value("${server.port}")
    private int serverPort;
    

    private Log log = LogFactory.getLog(ProducerController.class);
    private AtomicInteger counter = new AtomicInteger(0);

    @RequestMapping(value = "/", produces = "application/json")
    public String produce() {
        int value = counter.getAndIncrement();
        log.info("Produced a value: " + value);

        return String.format("{\"serverPort\":%d, \"value\":%d}", serverPort, value);
    }

}
