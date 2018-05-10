package com.anyun.esb.component.message;

import com.anyun.cloud.tools.StringUtils;
import com.anyun.common.jbi.JbiCommon;
import com.anyun.common.jbi.component.ComponentRegistInfo;
import com.anyun.common.jbi.component.ComponentServiceRegistInfo;
import com.anyun.common.jbi.component.ZKConnector;
import com.anyun.esb.component.message.core.ComponentServiceDefine;
import com.anyun.esb.component.message.core.amqp.AmqpMessageSender;
import com.anyun.exception.JbiComponentException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author twitchgg@yahoo.com
 * @version 1.0
 * @Date 4/13/16
 */
public class ComponentServiceProducer extends DefaultProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ComponentServiceProducer.class);
    private String componentName;
    private String componentZkPath;
    private String serviceZkPath;
    private String messageZkPath;

    public ComponentServiceProducer(Endpoint endpoint) {
        super(endpoint);
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        ZKConnector zk = ComponentEnv.getEnv().getQueryConnector();
        messageZkPath = "/anyuncloud/amqp";
        List<String> messageNodeSerialNumbers = zk.getChildren(messageZkPath);
        if (messageNodeSerialNumbers.size() == 0)
            throw buildException("Can not find runing message component [" + componentName + "]");
        componentZkPath = "/anyuncloud/component/" + componentName;
        LOGGER.debug("Component zookeeper path [{}]",componentZkPath);
        List<String> componentNodeNames = zk.getChildren(componentZkPath);
        if (componentNodeNames == null || componentNodeNames.size() == 0)
            throw buildException("Can not find runing component [" + componentName + "]");
        LOGGER.debug("loop component [{}] successfully", componentName);
        Object componentName = exchange.getIn().getHeader("business_component");
        if (componentName == null || !(componentName instanceof String)) {
            LOGGER.warn("Component name is not set");
            throw buildException("Service name is not set");
        }
        Object serviceName = exchange.getIn().getHeader("business_service");
        if (serviceName == null || !(serviceName instanceof String)) {
            LOGGER.warn("Service name is not set");
            throw buildException("Service name is not set");
        }
        String componentServiceName = String.valueOf(serviceName);
        serviceZkPath = "/anyuncloud/service/" + componentServiceName;
        List<String> services = zk.getChildren(serviceZkPath);
        if (services == null || services.isEmpty())
            throw buildException("Can not find runing component [" +
                    componentName + "] service [" + componentServiceName + "]");
        LOGGER.debug("loop component [{}] service [{}] successfully", componentName, componentServiceName);
        List<ComponentServiceDefine> defines =
                loopComponentService(zk, messageNodeSerialNumbers, componentNodeNames, services);
        if (defines.size() == 0)
            throw new Exception("Can not find runing component [" +
                    componentName + "] service [" + componentServiceName + "]");
        ComponentServiceDefine finalDefine = loopRandomServiceDefine(defines);
        LOGGER.debug("Service [{}] invoke ready", JbiCommon.toJson(finalDefine));
        LOGGER.debug("Service define size [{}]", defines.size());
        exchange.getIn().setHeader("anyuncloud_message_node_id", finalDefine.getMessageComponentId());
        exchange.getIn().setHeader("anyuncloud_service_name", serviceName);
        AmqpMessageSender amqpMessageSender = new AmqpMessageSender(exchange);
        amqpMessageSender.build().receive();
        LOGGER.debug("Produce size [{}]", ComponentEnv.getEnv().getMessageProducers().size());
    }

    private JbiComponentException buildException(String ex) {
        JbiComponentException exception = new JbiComponentException(1000,10001);
        exception.setMessage("Component service loop failed [" + ex + "]");
        exception.setUserMessage("组件服务查询失败[" + ex + "]");
        exception.setUserTitle("Service loop failed");
        return exception;
    }

    private ComponentServiceDefine loopRandomServiceDefine(List<ComponentServiceDefine> defines) {
        int size = defines.size();
        if (size == 1)
            return defines.get(0);
        LOGGER.debug("Finding random service define");
        return defines.get(new Random().nextInt(size - 1));
    }

    private List<ComponentServiceDefine> loopComponentService(ZKConnector zk, List<String> runningMessageNodes, List<String> runningComponents,
                                                              List<String> runningServices) throws Exception {
        List<ComponentServiceDefine> runningServiceDefine = new ArrayList<>();
        for (String runningService : runningServices) {
            String serviceDefineString = zk.getStringData(serviceZkPath + "/" + runningService);
            if (StringUtils.isEmpty(serviceDefineString)) {
                LOGGER.warn("Find wrong service info node [{}]", serviceDefineString);
                continue;
            }
            ObjectMapper mapper = new ObjectMapper();
            ComponentServiceRegistInfo componentServiceRegistInfo =
                    mapper.readValue(serviceDefineString, ComponentServiceRegistInfo.class);
            if (componentServiceRegistInfo == null) {
                LOGGER.warn("Get service info error [{}]", serviceDefineString);
                continue;
            }
            ComponentServiceDefine define = new ComponentServiceDefine();
            define.setComponentServiceRegistInfo(componentServiceRegistInfo);
            runningServiceDefine.add(define);
        }

        for (String runningComponent : runningComponents) {
            ComponentServiceDefine define = findServiceComponent(runningServiceDefine, runningComponent);
            if (define == null)
                continue;
            String componentDefineString = zk.getStringData(componentZkPath + "/" + runningComponent);
            if (StringUtils.isEmpty(componentDefineString)) {
                LOGGER.warn("Find wrong node [{}]", componentDefineString);
                continue;
            }
            ObjectMapper mapper = new ObjectMapper();
            ComponentRegistInfo componentRegistInfo = mapper.readValue(componentDefineString, ComponentRegistInfo.class);
            if (componentRegistInfo == null) {
                LOGGER.warn("Get component info error [{}]", componentDefineString);
                continue;
            }
            define.setComponentRegistInfo(componentRegistInfo);
        }

        for (String runningMessageNode : runningMessageNodes) {
            ComponentServiceDefine define = findServiceMessageNode(runningServiceDefine, runningMessageNode);
            if (define == null)
                continue;
            define.setMessageComponentId(runningMessageNode);
        }
        List<ComponentServiceDefine> running = new ArrayList<>();
        for (ComponentServiceDefine define : runningServiceDefine) {
            if (StringUtils.isNotEmpty(define.getMessageComponentId()))
                if (define.getComponentRegistInfo() != null && define.getComponentServiceRegistInfo() != null)
                    running.add(define);
        }
        return running;
    }

    private ComponentServiceDefine findServiceComponent(List<ComponentServiceDefine> serviceDefines, String componentSerial) {
        for (ComponentServiceDefine define : serviceDefines) {
            if (define.getComponentServiceRegistInfo().getComponent().equals(componentSerial))
                return define;
        }
        return null;
    }

    private ComponentServiceDefine findServiceMessageNode(List<ComponentServiceDefine> serviceDefines, String messageNode) {
        for (ComponentServiceDefine define : serviceDefines) {
            if(define.getComponentRegistInfo() == null)
                return null;
            if (define.getComponentRegistInfo().getMessageNode().equals(messageNode))
                return define;
        }
        return null;
    }


    @Override
    public void stop() throws Exception {
        super.stop();
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }
}
