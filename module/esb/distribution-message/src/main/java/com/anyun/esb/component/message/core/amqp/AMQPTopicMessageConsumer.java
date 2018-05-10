/*
 *     Licensed to the Apache Software Foundation (ASF) under one or more
 *     contributor license agreements.  See the NOTICE file distributed with
 *     this work for additional information regarding copyright ownership.
 *     The ASF licenses this file to You under the Apache License, Version 2.0
 *     (the "License"); you may not use this file except in compliance with
 *     the License.  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package com.anyun.esb.component.message.core.amqp;

import com.anyun.common.jbi.component.ConsumerThread;
import com.anyun.esb.component.message.ComponentEnv;
import org.apache.camel.Component;
import org.apache.camel.Endpoint;
import org.apache.camel.Processor;
import org.apache.qpid.jms.JmsTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 4/12/16
 */
public class AMQPTopicMessageConsumer extends ConsumerThread {
    private static final Logger LOGGER = LoggerFactory.getLogger(AMQPTopicMessageConsumer.class);
    private JmsTopic topic;
    private int timeout = 1000;

    public AMQPTopicMessageConsumer(Component component, Endpoint endpoint, Processor processor) {
        super(component, endpoint, processor);
        topic = new JmsTopic(AmqFactory.QUEUE_ANYUN_MSG);
    }

    @Override
    public void run() {
        try {
            Jedis jedis = new Jedis("redis.dev.anyuncloud.com", 6379);
            String addr = jedis.get("com.anyun.message.amqp.address");
            String user = jedis.get("com.anyun.message.amqp.user");
            String passwd = jedis.get("com.anyun.message.amqp.passwd");
            AmqFactory.getFactory().connect(addr, user, passwd);
            AmqFactory.getFactory().initSessesionPool(10);
            Session session = AmqFactory.getFactory().getRandomSession();
            ComponentEnv.getEnv().setAmqpConnectSession(session);
            MessageConsumer messageConsumer = session.createConsumer(topic);
            while (isStart()) {
                Message message = null;
                if (timeout == 0)
                    message = messageConsumer.receiveNoWait();
                else
                    message = messageConsumer.receive(timeout);
                if (message == null)
                    continue;
                new MessageProcess(message, getEndpoint()).start();
            }
            AmqFactory.getFactory().closeConnection();
        } catch (Exception ex) {
            ex.printStackTrace();
            stop();
        }
    }

    @Override
    public void stop() {
        super.stop();
        LOGGER.debug("Shutdown AMQPTopicMessageConsumer ");
        AmqFactory.getFactory().closeConnection();
    }


    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public JmsTopic getTopic() {
        return topic;
    }
}
