/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.sample.servlet;

import es.sample.http.Main;
import es.sample.jmx.model.ModelHelloFactory;
import java.lang.management.ManagementFactory;
import java.net.URL;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import net.sf.snmpadaptor4j.SnmpAdaptor;

/**
 * Listener that loads the Hello JMX bean.
 * 
 * @author ricky
 */
@WebListener
public class JmxListener implements ServletContextListener {

    public final static String JMX_TYPE = "es.sample.jmx.type";
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext sc = sce.getServletContext();
        String type = sc.getInitParameter(JMX_TYPE);
        Object mbean = null;
        if (type != null && type.equals("model")) {
            System.err.println("Using model JMX Bean");
            mbean = new es.sample.jmx.standard.Hello();
        } else if (type != null && type.equals("dynamic")) {
            System.err.println("Using dynamic JMX Bean");
            mbean = new es.sample.jmx.dynamic.Hello();
        } else if (type != null && type.equals("mxbean")) {
            System.err.println("Using mxbean JMX Bean");
            mbean = new es.sample.jmx.mx.Hello();
        } else {
            System.err.println("Using standard JMX Bean");
            mbean = new es.sample.jmx.standard.Hello();
        }
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            ObjectName name = new ObjectName("es.sample.jmx:type=HelloMBean");
            if (type != null && type.equals("model")) {
                // case (c) model mbean
                mbs.registerMBean(ModelHelloFactory.create((es.sample.jmx.standard.Hello) mbean), name);
            } else {
                //  case (a) standard mbean and (b) dynamic mbean and (d) MXBean
                mbs.registerMBean(mbean, name);
            }
            sc.setAttribute("es.sample.jmx.bean", mbean);
            // register the SNMP agent
            URL spmlConfig = Main.class.getResource("/snmp.xml");
            SnmpAdaptor snmpAdaptor = new SnmpAdaptor(spmlConfig, true);
            ObjectName snmpAdaptorMBeanName = new ObjectName("net.sf.snmpadaptor4j.example:adaptor=SnmpAdaptor");
            mbs.registerMBean(snmpAdaptor, snmpAdaptorMBeanName);
            snmpAdaptor.start();
        } catch (Exception e) {
            System.err.println("Error creating and registering the bean...");
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            ObjectName name = new ObjectName("es.sample.jmx:type=HelloMBean");
            mbs.unregisterMBean(name);
        } catch (Exception e) {
            System.err.println("Error unregistering the bean...");
            e.printStackTrace();
        }
    }
    
}
