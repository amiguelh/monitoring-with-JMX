/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.sample.client;

import com.sun.tools.attach.VirtualMachine;
import es.sample.jmx.standard.HelloMBean;
import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

/**
 *
 * @author ricky
 */
public class HelloStandardClient {

    static public void main(String[] args) throws Exception {
        if (args.length != 1) {
            throw new IllegalArgumentException("The PID should be passed as an argument");
        }
        String pid = args[0];
        String jmxUrl = null;
        VirtualMachine vm = null;
        try {
            System.err.println("Connectin to JVM with pid " + pid + "...");
            vm = VirtualMachine.attach(pid);
            jmxUrl = vm.getAgentProperties().getProperty("com.sun.management.jmxremote.localConnectorAddress");
            if (jmxUrl == null) {
                // start the agent
                System.err.println("The JMX agent is not started inside the JVM. Starting it...");
                String javaHome = vm.getSystemProperties().getProperty("java.home");
                String agent = javaHome + "/lib/management-agent.jar";
                vm.loadAgent(agent);
                // read again once it is started
                jmxUrl = vm.getAgentProperties().getProperty("com.sun.management.jmxremote.localConnectorAddress");
                if (jmxUrl == null) {
                    throw new IllegalStateException("Agent not loaded!");
                }
            }
        } finally {
            if (vm != null) {
                vm.detach();
            }
        }
        if (jmxUrl == null) {
            throw new IllegalStateException("No JMX URL found for the process!");
        }
        System.err.println("The JMX URL is " + jmxUrl + ". Connecting to the agent...");
        JMXServiceURL jmxsu = new JMXServiceURL(jmxUrl);
        try (JMXConnector jmxc = JMXConnectorFactory.connect(jmxsu, null)) {
            MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();
            ObjectName mbeanName = new ObjectName("es.sample.jmx:type=HelloMBean");
            System.err.println("Instantiating the HelloMBean...");
            HelloMBean hello = JMX.newMBeanProxy(mbsc, mbeanName, HelloMBean.class, true);
            System.out.println("Message: " + hello.getMessage());
            System.out.println("Hellos: " + hello.getHellos());
            System.out.println("World hellos: " + hello.getWorldHellos());
            System.out.println("Named hellos: " + hello.getNamedHellos());
        }
    }

}
