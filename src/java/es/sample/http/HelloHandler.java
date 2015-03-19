/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.sample.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import es.sample.jmx.mx.HelloMXBean;
import es.sample.jmx.standard.HelloMBean;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * http://docs.oracle.com/javase/tutorial/jmx/
 * 
 * @author ricky
 */
public class HelloHandler implements HttpHandler {

    private HelloMXBean mxbean = null;
    private HelloMBean mbean = null;
    
    public HelloHandler(Object mbean) {
        if (mbean instanceof HelloMXBean) {
            this.mxbean = (HelloMXBean) mbean;
        } else if (mbean instanceof HelloMBean) {
            this.mbean = (HelloMBean) mbean;
        } else {
            throw new IllegalArgumentException("The bean is not a JMX bean!");
        }
    }
    
    public static Map<String, String> splitQuery(URI uri) throws UnsupportedEncodingException {
        Map<String, String> query_pairs = new HashMap<>();
        String query = uri.getQuery();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
        }
        return query_pairs;
    }
    
    @Override
    public void handle(HttpExchange he) throws IOException {
        URI uri = he.getRequestURI();
        Map<String, String> map = splitQuery(uri);
        String name = map.get("name");
        String response = (mbean != null)? mbean.sayHello(name) : mxbean.sayHello(name);
        he.sendResponseHeaders(200, response.length());
        try (OutputStream os = he.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
    
}
