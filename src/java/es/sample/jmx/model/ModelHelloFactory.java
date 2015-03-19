/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.sample.jmx.model;

import es.sample.jmx.standard.Hello;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.Notification;
import javax.management.RuntimeOperationsException;
import javax.management.modelmbean.DescriptorSupport;
import javax.management.modelmbean.InvalidTargetObjectTypeException;
import javax.management.modelmbean.ModelMBean;
import javax.management.modelmbean.ModelMBeanAttributeInfo;
import javax.management.modelmbean.ModelMBeanInfoSupport;
import javax.management.modelmbean.ModelMBeanNotificationInfo;
import javax.management.modelmbean.ModelMBeanOperationInfo;
import javax.management.modelmbean.RequiredModelMBean;

/**
 *
 * @author ricky
 */
public class ModelHelloFactory {

    static public ModelMBean create(Hello mbean) throws MBeanException, InstanceNotFoundException, 
            RuntimeOperationsException, InvalidTargetObjectTypeException {
        ModelMBeanInfoSupport info = new ModelMBeanInfoSupport(
                Hello.class.getName(),
                "The Hello bean sample",
                new ModelMBeanAttributeInfo[]{
                    new ModelMBeanAttributeInfo("Message",
                            String.class.getName(),
                            "The hello message to display.",
                            true,
                            true,
                            false,
                            new DescriptorSupport(
                                    new String[] {
                                        "name=Message",
                                        "descriptorType=attribute",
                                        "default=Hello",
                                        "getMethod=getMessage",
                                        "setMethod=setMessage",
                                    }
                            )
                    ),
                    new ModelMBeanAttributeInfo("Hellos",
                            Long.class.getName(),
                            "The number of hellos sent by the servlet.",
                            true,
                            false,
                            false,
                            new DescriptorSupport(
                                    new String[] {
                                        "name=Hellos",
                                        "default=0",
                                        "descriptorType=attribute",
                                        "getMethod=getHellos",
                                    }
                            )
                    ),
                    new ModelMBeanAttributeInfo("WorldHellos",
                            Long.class.getName(),
                            "The number of world hellos (no name speficied) sent by the servlet.",
                            true,
                            false,
                            false,
                            new DescriptorSupport(
                                    new String[] {
                                        "name=WorldHellos",
                                        "descriptorType=attribute",
                                        "getMethod=getWorldHellos",
                                    }
                            )
                    ),
                    new ModelMBeanAttributeInfo("NamedHellos",
                            Long.class.getName(),
                            "The number of names hellos (name specified) sent by the servlet.",
                            true,
                            false,
                            false,
                            new DescriptorSupport(
                                    new String[]{
                                        "name=NamedHellos",
                                        "descriptorType=attribute",
                                        "getMethod=getNamedHellos",
                                    }
                            )
                    ),
                },
                null,
                new ModelMBeanOperationInfo[]{
                    new ModelMBeanOperationInfo(
                            "reset",
                            "Reset the number of hellos and put default hello message.",
                            null,
                            "void",
                            MBeanOperationInfo.ACTION
                    ),
                    new ModelMBeanOperationInfo(
                            "sayHello",
                            "Method to say hello to somebody.",
                            new MBeanParameterInfo[] {
                                new MBeanParameterInfo("name", String.class.getName(), "The name of the person")
                            },
                            String.class.getName(),
                            MBeanOperationInfo.ACTION
                    ),
                    new ModelMBeanOperationInfo(
                            "getMessage",
                            "Getter for the hello message to display.",
                            null,
                            String.class.getName(),
                            MBeanOperationInfo.INFO
                    ),
                    new ModelMBeanOperationInfo(
                            "setMessage",
                            "Setter for the hello message to display.",
                            new MBeanParameterInfo[] {
                                new MBeanParameterInfo("message", String.class.getName(), "The new message")
                            },
                            "void",
                            MBeanOperationInfo.ACTION
                    ),
                    new ModelMBeanOperationInfo(
                            "getHellos",
                            "Getter for the number of hellos sent by the servlet.",
                            null,
                            Long.class.getName(),
                            MBeanOperationInfo.INFO
                    ),
                    new ModelMBeanOperationInfo(
                            "getWorldHellos",
                            "Getter for the number of world hellos sent by the servlet.",
                            null,
                            Long.class.getName(),
                            MBeanOperationInfo.INFO
                    ),
                    new ModelMBeanOperationInfo(
                            "getNamedHellos",
                            "Getter for the number of named hellos sent by the servlet.",
                            null,
                            Long.class.getName(),
                            MBeanOperationInfo.INFO
                    ),
                },
                new ModelMBeanNotificationInfo[] {
                    new ModelMBeanNotificationInfo(
                            new String[]{"Hello"},
                            Notification.class.getName(),
                            "Hello notification displayed when saying hello."
                    ),
                }
        );
        RequiredModelMBean model = new RequiredModelMBean(info);
        mbean.setSource(model);
        model.setManagedResource(mbean, "ObjectReference");
        return model;
    }
}
