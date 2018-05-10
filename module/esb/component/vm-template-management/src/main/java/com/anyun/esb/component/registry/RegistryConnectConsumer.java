package com.anyun.esb.component.registry;

import com.anyun.common.jbi.component.ConsumerThread;
import com.anyun.common.jbi.component.ServiceInvoker;
import com.anyun.esb.component.registry.common.Env;
import com.anyun.esb.component.registry.common.HostSshClient;
import org.apache.camel.Component;
import org.apache.camel.Endpoint;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;



/**
 * @author twitchgg@yahoo.com
 * @version 1.0
 * @Date 7/4/16
 */
public class RegistryConnectConsumer extends ConsumerThread {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegistryConnectConsumer.class);
    private Component component;
    private Endpoint endpoint;
    private Processor processor;

    public RegistryConnectConsumer(Component component, Endpoint endpoint, Processor processor) {
        this.component = component;
        this.endpoint = endpoint;
        this.processor = processor;
    }

    @Override
    public void run() {
        Jedis jedis = Env.env(Jedis.class);
        String registryHostKey = jedis.get("com.anyun.certs.path") + "/registry_rsa";
        String registryStorageHost = jedis.get("com.anyun.docker.registry.storage.host");
        try {
            LOGGER.debug("Key path [{}]",registryHostKey);
            HostSshClient hostSshClient = new HostSshClient(registryHostKey);
            hostSshClient.setHost(registryStorageHost);
            hostSshClient.connect();
            Env.set("REGISTRY.CLIENT", hostSshClient);
            LOGGER.debug("Set registry client [{}]",Env.env(HostSshClient.class,"REGISTRY.CLIENT"));
        } catch (Exception ex) {
            LOGGER.error("Registry host connect error", ex);
            stop();
        }
    }

    @Override
    public Component getComponent() {
        return component;
    }

    @Override
    public Endpoint getEndpoint() {
        return endpoint;
    }

    @Override
    public Processor getProcessor() {
        return processor;
    }
}
