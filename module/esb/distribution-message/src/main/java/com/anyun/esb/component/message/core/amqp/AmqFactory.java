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

import com.anyun.esb.component.message.core.MessageConnectionExceptionListener;
import org.apache.qpid.jms.JmsConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 4/13/16
 */
public class AmqFactory {
    public static final Logger LOGGER = LoggerFactory.getLogger(AmqFactory.class);
    public static final String QUEUE_ANYUN_MSG = "amq.topic";

    private static AmqFactory factory;
    private JmsConnectionFactory connectionFactory;
    private Connection connection;
    private List<Session> sessions;
    private int sessionPoolSize = 100;

    private AmqFactory() {
        sessions = new ArrayList<>();
    }

    public static AmqFactory getFactory() {
        synchronized (AmqFactory.class) {
            if (factory == null)
                factory = new AmqFactory();
        }
        return factory;
    }

    public void connect(String addr, String user, String passwd) throws Exception {
        LOGGER.debug("AMQP connection address [{}]", addr);
        LOGGER.debug("AMQP connection user [{}] passwd [{}]", user, passwd);
        connectionFactory = new JmsConnectionFactory(addr);
        connection = connectionFactory.createConnection(user, passwd);
        connection.setExceptionListener(new MessageConnectionExceptionListener());
        connection.start();
        LOGGER.info("-- AnyunCloud AMQP message server connected,recive message from [{}] --", addr);
    }

    public void initSessesionPool(int sessionPoolSize) throws Exception {
        this.sessionPoolSize = sessionPoolSize;
        for (int i = 0; i < sessionPoolSize; i++) {
            Session session = getConnection().createSession(false, Session.AUTO_ACKNOWLEDGE);
            sessions.add(session);
        }
        LOGGER.info("AnyunCloud AMQP connection session pool created,pool size [{}]", sessions.size());
    }

    public Session getRandomSession() throws Exception {
        int index = new Random().nextInt(sessions.size());
        Session session = sessions.get(index);
        if (session == null)
            return getRandomSession();
        return session;
    }

    public Session releaseSession() throws Exception {
        if(sessions.isEmpty())
            initSessesionPool(100);
        int index = new Random().nextInt(sessions.size());
        Session session = sessions.get(index);
        try {
            session.createTextMessage();
        } catch (javax.jms.IllegalStateException ex) {
            sessions.remove(index);
            releaseSession();
        }
        return session;
    }

    public JmsConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }

    public void closeConnection() {
        try {
            if (connection != null)
                connection.close();
        } catch (JMSException e) {
            LOGGER.warn("AMQP connection close error [{}]", e.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public int getSessionPoolSize() {
        return sessionPoolSize;
    }
}
