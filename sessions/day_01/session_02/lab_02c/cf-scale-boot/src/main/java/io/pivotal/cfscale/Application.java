package io.pivotal.cfscale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.Cloud;
import org.springframework.cloud.CloudFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.concurrent.atomic.AtomicLong;

@Controller
@SpringBootApplication
public class Application {

    private AtomicLong counter = new AtomicLong();

    @Autowired
    Cloud cloud;

    @RequestMapping("/")
    String index(Model model) {

        model.addAttribute("instance", cloud.getApplicationInstanceInfo().getInstanceId());
        model.addAttribute("props", cloud.getApplicationInstanceInfo().getProperties());
        model.addAttribute("requestsServed", counter.incrementAndGet());

        return "index";
    }

    @RequestMapping("/killSwitch")
    void killMe() {
        System.exit(1);
    }

    @Bean
    Cloud getCloud() {
        return new CloudFactory().getCloud();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
