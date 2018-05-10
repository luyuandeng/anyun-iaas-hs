package com.anyun.esb.component.message;

import com.anyun.common.jbi.component.ZKConnector;
import com.anyun.esb.component.message.core.amqp.AmqFactory;
import com.anyun.esb.component.message.core.amqp.AmqpMessageProducer;
import org.apache.zookeeper.KeeperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import javax.jms.Session;
import java.util.ArrayList;
import java.util.List;

/**
 * @author twitchgg@yahoo.com
 * @version 1.0
 * @Date 4/15/16
 */
public class ComponentEnv {
    private static Logger LOGGER = LoggerFactory.getLogger(ComponentEnv.class);
    private static ComponentEnv env;
    private List<AmqpMessageProducer> messageProducers;
    private Jedis jedis;
    private ZKConnector queryConnector;
    private String localMessageComponentSerialNumber;
    private Session amqpConnectSession;

    private ComponentEnv() {
        messageProducers = new ArrayList<>();
    }

    public static ComponentEnv getEnv() {
        synchronized (ComponentEnv.class) {
            if (env == null)
                env = new ComponentEnv();
        }
        return env;
    }

    public List<AmqpMessageProducer> getMessageProducers() {
        return messageProducers;
    }

    public void setMessageProducers(List<AmqpMessageProducer> messageProducers) {
        this.messageProducers = messageProducers;
    }

    public void removeMessageProducer(String messageId) {
        AmqpMessageProducer _remove = null;
        for (AmqpMessageProducer producer : messageProducers) {
            if (producer.getMessageId().equals(messageId)) {
                _remove = producer;
                break;
            }
        }
        messageProducers.remove(_remove);
    }

    public Jedis getJedis() {
        if (jedis == null || !jedis.isConnected()) {
            jedis = new Jedis("redis", 6379);
        }
        return jedis;
    }

    public void setJedis(Jedis jedis) {
        this.jedis = jedis;
    }

    public ZKConnector getQueryConnector() throws Exception {
        if (queryConnector == null) {
            String zkString = jedis.get("com.anyun.zookeeper.address");
            String timeout = jedis.get("com.anyun.zookeeper.timeout");
            queryConnector = new ZKConnector();
            queryConnector.connect(zkString, Long.valueOf(timeout).intValue());
        }
        try {
            queryConnector.exist("/anyuncloud");
        } catch (Exception exception) {
            if (exception instanceof KeeperException) {
                LOGGER.debug("reconnect zookeeper");
                queryConnector.close();
                queryConnector = null;
                getQueryConnector();
            }
        }
        return queryConnector;
    }

    public String getLocalMessageComponentSerialNumber() {
        return localMessageComponentSerialNumber;
    }

    public void setLocalMessageComponentSerialNumber(String localMessageComponentSerialNumber) {
        this.localMessageComponentSerialNumber = localMessageComponentSerialNumber;
    }

    public Session getAmqpConnectSession() {
//        return amqpConnectSession;
        try {
            return AmqFactory.getFactory().getRandomSession();
        } catch (Exception e) {
            return null;
        }
    }

    public void setAmqpConnectSession(Session amqpConnectSession) {
        this.amqpConnectSession = amqpConnectSession;
    }
}
