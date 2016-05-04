package io.springtrader.authserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.security.oauth2.resource.EnableOAuth2Resource;

/**
 * Created by vcarvalho on 5/4/15.
 */
@SpringBootApplication
@EnableOAuth2Resource
public class SpringtraderResourceServerApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(SpringtraderResourceServerApplication.class);
        app.run(args);
    }


}
