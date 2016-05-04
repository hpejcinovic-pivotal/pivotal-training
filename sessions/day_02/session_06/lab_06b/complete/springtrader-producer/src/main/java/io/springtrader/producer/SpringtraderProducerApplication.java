package io.springtrader.producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SpringtraderProducerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringtraderProducerApplication.class, args);
    }

}
