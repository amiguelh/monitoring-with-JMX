/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.sample.jmx.mx;


/**
 *
 * @author ricky
 */
public interface HelloMXBean {
    
    public String getMessage();
    
    public void setMessage(String message);
    
    public long getHellos();
    
    public long getWorldHellos();
    
    public long getNamedHellos();
    
    public String sayHello(String name);
    
    public void reset();
    
    public Statistics getStatistics();
    
}
