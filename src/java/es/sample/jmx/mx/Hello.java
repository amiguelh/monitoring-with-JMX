/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.sample.jmx.mx;

import javax.management.MBeanException;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import javax.management.modelmbean.ModelMBean;

/**
 *
 * @author ricky
 */
public class Hello implements HelloMXBean {

    private String message = null;
    private long namedHellos = 0;
    private long worldHellos = 0;
    private Object source = this;
    
    public Hello() {
        message = "Hello";
        namedHellos = 0;
        worldHellos = 0;
    }
    
    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public long getHellos() {
        return namedHellos + worldHellos;
    }

    @Override
    public long getWorldHellos() {
        return worldHellos;
    }

    @Override
    public long getNamedHellos() {
        return namedHellos;
    }
    
    synchronized private void incrementNamedHellos() {
        namedHellos++;
    }
    
    synchronized private void incrementWorldHellos() {
        worldHellos++;
    }
    
    @Override
    public String sayHello(String name) {
        if (name == null || name.isEmpty()) {
            name = "world";
            this.incrementWorldHellos();
        } else {
            this.incrementNamedHellos();
        }
        String txt = this.message + " " + name + "!!!";
        Notification n = new Notification("Hello", source, this.getHellos(), System.currentTimeMillis(), txt);
        System.err.println("Sending notifiocation: " + n);
        try {
            if (source instanceof NotificationBroadcasterSupport) {
                ((NotificationBroadcasterSupport) source).sendNotification(n);
            } else if (source instanceof ModelMBean) {
                ((ModelMBean) source).sendNotification(n);
            }
        } catch (MBeanException e) {
            e.printStackTrace();
        }
        return txt;
    }
    
    public void setSource(Object source) {
        this.source = source;
    }
    
    @Override
    synchronized public void reset() {
        message = "Hello";
        namedHellos = 0;
        worldHellos = 0;
    }
    
    @Override
    public Statistics getStatistics() {
        return new Statistics(getHellos(), getNamedHellos(), getWorldHellos());
    }
    
}
