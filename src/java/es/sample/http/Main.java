/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.sample.http;

import com.sun.net.httpserver.HttpServer;
import es.sample.jmx.model.ModelHelloFactory;
import java.lang.management.ManagementFactory;
import java.net.InetSocketAddress;
import java.net.URL;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import net.sf.snmpadaptor4j.SnmpAdaptor;

/**
 *
 * @author ricky
 */
public class Main {
    
    static public void main(String[] args) throws Exception {
        // creaion of the MBean
        Object mbean = null;
        if (args.length > 0 && args[0].equals("model")) {
            System.err.println("Using model JMX Bean");
            mbean = new es.sample.jmx.standard.Hello();
        } else if (args.length > 0 && args[0].equals("dynamic")) {
            System.err.println("Using dynamic JMX Bean");
            mbean = new es.sample.jmx.dynamic.Hello();
        } else if (args.length > 0 && args[0].equals("mxbean")) {
            System.err.println("Using mxbean JMX Bean");
            mbean = new es.sample.jmx.mx.Hello();
        } else {
            System.err.println("Using standard JMX Bean");
            mbean = new es.sample.jmx.standard.Hello();
        }
        // registration of the MBean
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("es.sample.jmx:type=HelloMBean");
        if (args.length > 0 && args[0].equals("model")) {
            // case (c) model mbean
            mbs.registerMBean(ModelHelloFactory.create((es.sample.jmx.standard.Hello) mbean), name);
        } else {
            //  case (a) standard mbean and (b) dynamic mbean and (d) MXBean
            mbs.registerMBean(mbean, name);
        }
        // register the SNMP agent
        URL spmlConfig = Main.class.getResource("/snmp.xml");
        SnmpAdaptor snmpAdaptor = new SnmpAdaptor(spmlConfig, true);
        ObjectName snmpAdaptorMBeanName = new ObjectName("net.sf.snmpadaptor4j.example:adaptor=SnmpAdaptor");
 	mbs.registerMBean(snmpAdaptor, snmpAdaptorMBeanName);
 	snmpAdaptor.start();
        // start the HTTP server
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 100);
        server.createContext("/applications/hello", new HelloHandler(mbean));
        server.setExecutor(null); // creates a default executor
        server.start();
    }
}
