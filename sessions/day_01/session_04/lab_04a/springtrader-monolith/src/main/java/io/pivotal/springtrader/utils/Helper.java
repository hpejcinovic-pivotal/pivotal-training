package io.pivotal.springtrader.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.ModelAndView;



/**
 * Created by cax on 25/11/2015.
 */
public class Helper {


    public static ModelAndView generateError(Exception exception){
        ModelAndView model = new ModelAndView();
        model.addObject("errorCode", exception.getMessage());
        model.addObject("errorMessage", exception);
        model.setViewName("error");
        exception.printStackTrace();
        return model;
    }

    public static HttpHeaders getNoCacheHeaders() {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Cache-Control", "no-cache");
        return responseHeaders;
    }
}
