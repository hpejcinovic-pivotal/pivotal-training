package io.pivotal.springtrader.accounts.config;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
@ComponentScan(basePackages = { "io.pivotal.springtrader.accounts" })
public class WebConfig extends WebMvcConfigurationSupport {

    /**
     * configure the message converters with the date formatter.
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {

        MappingJackson2HttpMessageConverter mappingJacksonHttpMessageConverter = new MappingJackson2HttpMessageConverter();
        mappingJacksonHttpMessageConverter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON));

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
        mappingJacksonHttpMessageConverter.getObjectMapper().setDateFormat(format);

        converters.add(mappingJacksonHttpMessageConverter);
    }

}
