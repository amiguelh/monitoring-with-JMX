/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.sample.jmx.dynamic;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.Notification;
import javax.management.ReflectionException;

/**
 *
 * @author ricky
 */
public class Hello extends es.sample.jmx.standard.Hello implements DynamicMBean {

    public Hello() {
        super();
    }

    @Override
    public Object getAttribute(String attr) throws AttributeNotFoundException, MBeanException, ReflectionException {
        switch (attr) {
            case "Message":
                return getMessage();
            case "Hellos":
                return getHellos();
            case "WorldHellos":
                return getWorldHellos();
            case "NamedHellos":
                return getNamedHellos();
            default:
                throw new AttributeNotFoundException("Unknown attribute: " + attr);
        }
    }

    @Override
    public void setAttribute(Attribute attr) throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException {
        if ("Message".equals(attr.getName())) {
            Object val = attr.getValue();
            if (val instanceof String) {
                String v = (String) val;
                this.setMessage(v);
            } else {
                throw new InvalidAttributeValueException("Message attribute should be a String");
            }
        } else {
            throw new AttributeNotFoundException("Unknown attribute: " + attr);
        }
    }

    @Override
    public AttributeList getAttributes(String[] attrs) {
        AttributeList result = new AttributeList();
        if (attrs != null) {
            for (String attr: attrs) {
                try {
                    Object res = this.getAttribute(attr);
                    result.add(new Attribute(attr, res));
                } catch (AttributeNotFoundException | MBeanException | ReflectionException e) {
                    // continue processing
                }
            }
        }
        return result;
    }

    @Override
    public AttributeList setAttributes(AttributeList al) {
        AttributeList result = new AttributeList();
        if (al != null) {
            for (Attribute attr: al.asList()) {
                try {
                    setAttribute(attr);
                    result.add(attr);
                } catch (AttributeNotFoundException | MBeanException | InvalidAttributeValueException | ReflectionException e) {
                    // continue
                }
            }
        }
        return result;
    }

    @Override
    public Object invoke(String opName, Object[] params, String[] signature) throws MBeanException, ReflectionException {
        if ("reset".equals(opName)) {
            this.reset();
            return null;
        } else if ("sayHello".equals(opName)) {
            return this.sayHello((String) params[0]);
        } else {
            throw new ReflectionException(new NoSuchMethodException(opName), 
                "Unknown operation: " + opName);
        }
    }

    @Override
    public MBeanInfo getMBeanInfo() {
        return new MBeanInfo(
                this.getClass().getName(), 
                "The Hello bean sample", 
                new MBeanAttributeInfo[]{ 
                    new MBeanAttributeInfo("Message", 
                            String.class.getName(),
                            "The hello message to display.",
                            true,
                            true,
                            false
                    ),
                    new MBeanAttributeInfo("Hellos", 
                            Long.class.getName(),
                            "The number of hellos sent by the servlet.",
                            true,
                            false,
                            false
                    ),
                    new MBeanAttributeInfo("WorldHellos", 
                            Long.class.getName(),
                            "The number of world hellos (no name speficied) sent by the servlet.",
                            true,
                            false,
                            false
                    ),
                    new MBeanAttributeInfo("NamedHellos", 
                            Long.class.getName(),
                            "The number of names hellos (name specified) sent by the servlet.",
                            true,
                            false,
                            false
                    ),
                }, 
                null, 
                new MBeanOperationInfo[] {
                    new MBeanOperationInfo(
                            "reset",
                            "Reset the number of hellos and put default hello message.",
                            null,
                            "void",
                            MBeanOperationInfo.ACTION
                    ),
                    new MBeanOperationInfo(
                            "sayHello",
                            "Method to say hello to somebody.",
                            new MBeanParameterInfo[]{ 
                                new MBeanParameterInfo("name", String.class.getName(), "The name of the person"),
                            },
                            String.class.getName(),
                            MBeanOperationInfo.ACTION
                    ),
                }, 
                new MBeanNotificationInfo[] {
                    new MBeanNotificationInfo(
                            new String[]{"Hello"},
                            Notification.class.getName(),
                            "Hello notification displayed when saying hello."
                    ),
                }
        );
    }
}
