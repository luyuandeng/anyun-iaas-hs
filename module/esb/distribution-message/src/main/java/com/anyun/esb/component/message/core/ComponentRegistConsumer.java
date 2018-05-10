package com.anyun.esb.component.message.core;

import com.anyun.common.jbi.JbiCommon;
import com.anyun.common.jbi.component.*;
import com.anyun.esb.component.message.ComponentEnv;
import org.apache.camel.Component;
import org.apache.camel.Endpoint;
import org.apache.camel.Processor;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author twitchgg@yahoo.com
 * @version 1.0
 * @Date 4/14/16
 */
public class ComponentRegistConsumer extends ZKBaseConsumerThread {
    private static final Logger LOGGER = LoggerFactory.getLogger(ComponentRegistConsumer.class);

    public ComponentRegistConsumer(Component component, Endpoint endpoint, Processor processor) {
        super(component, endpoint, processor);
    }

    @Override
    protected void _run() throws Exception {
        zookeeperConnector.setConnectLostListener(new ZkConnectLostListener() {
            @Override
            public void afterConnectLost(ZKConnector connector) {
                try {
                    stop();
                    new ComponentRegistConsumer(getComponent(), getEndpoint(), getProcessor()).start();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        String serialNumber = ComponentUtil.getMessageComponentSerialNumber();
        String zkPath = jedis.get("com.anyun.zookeeper.path.amqp");
        zookeeperConnector.createIfNodeNotExist(zkPath, null, CreateMode.PERSISTENT);
        zkPath = zkPath + serialNumber;
        ComponentRegistInfo componentRegistInfo = new ComponentRegistInfo();
        String payload = JbiCommon.toJson(componentRegistInfo);
        zookeeperConnector.createIfNodeNotExist(zkPath, payload, CreateMode.EPHEMERAL);
        LOGGER.info("AnyunCloud Message Component [{}] registed", serialNumber);
        ComponentEnv.getEnv().setJedis(jedis);
        ComponentEnv.getEnv().setLocalMessageComponentSerialNumber(serialNumber);
    }
}
