package com.anyun.esb.component.message.core.amqp;

import com.anyun.cloud.tools.StringUtils;
import com.anyun.esb.component.message.ComponentEnv;
import com.anyun.esb.component.message.core.service.ServiceMessageRequestProcess;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.qpid.jms.JmsTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.lang.reflect.Method;

/**
 * @author twitchgg@yahoo.com
 * @version 1.0
 * @Date 7/5/16
 */
public class MessageProcess implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageProcess.class);
    private Thread thread;
    private Message message;
    private Endpoint endpoint;

    public MessageProcess(Message message, Endpoint endpoint) {
        this.message = message;
        this.endpoint = endpoint;
    }

    public void start() {
        this.thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        try {
            String messageId = message.getStringProperty("anyuncloud_message_id");
            String messageType = message.getStringProperty("anyuncloud_message_type");
            if (StringUtils.isEmpty(messageType))
                return;
            LOGGER.debug("Recived message id [{}],message type [{}]", messageId, messageType);
            if (messageType.equals("request")) {
                String nodeId = message.getStringProperty("anyuncloud_message_node_id");
                String localNodeId = ComponentEnv.getEnv().getLocalMessageComponentSerialNumber();
                LOGGER.debug("Received message node id [{}],local node id [{}]", nodeId, localNodeId);
                if (!nodeId.equals(localNodeId))
                    return;
                ServiceMessageRequestProcess serviceMessageRequestProcess = new ServiceMessageRequestProcess();
                serviceMessageRequestProcess.process(endpoint, messageId, message);
            } else if (messageType.equals("response")) {
                responseProcess(messageId, message);
            } else {
                throw new UnsupportedOperationException("Unsupported message type [" + messageType + "]");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.warn("Message process error [{}],building error message entity", ex.getMessage());
            try {
                Session session = ComponentEnv.getEnv().getAmqpConnectSession();
                String messageId = message.getStringProperty("anyuncloud_message_id");
                TextMessage errorMessage = session.createTextMessage();
                errorMessage.setStringProperty("anyuncloud_message_id", messageId);
                errorMessage.setStringProperty("anyuncloud_message_type", "response");
                errorMessage.setStringProperty("anyuncloud_message_response_type", "message_error");
                LOGGER.warn("Exception type [{}]", ex.getClass().getName());
                if (ex.getClass().getName().equals(JbiComponentException.class.getName())) {
                    errorMessage.setStringProperty("anyuncloud_exception_type",
                            JbiComponentException.class.getSimpleName());
//                    JbiComponentException exception = (JbiComponentException) ex;
//                    JbiExceptionEntity entity = new JbiExceptionEntity();
//                    entity.setUserTitle(exception.getUserTitle());
//                    entity.setMessage(exception.getMessage());
//                    entity.setUserMessage(exception.getUserMessage());
//                    entity.setCode(exception.getCode());
//                    entity.setSubCode(exception.getSubCode());
                    JbiExceptionEntity entity = getJbiExceptionEntity(ex);
                    String json = entity.asJson();
                    LOGGER.warn("JBIException json [\n{}\n]", json);
                    errorMessage.setText(json);
                } else {
                    errorMessage.setStringProperty("anyuncloud_exception_type", ex.getClass().getSimpleName());
                    errorMessage.setText(ex.getMessage());
                }
                MessageProducer errorMessageProducer =
                        session.createProducer(new JmsTopic(AmqFactory.QUEUE_ANYUN_MSG));
                errorMessageProducer.send(errorMessage,
                        DeliveryMode.NON_PERSISTENT,
                        Message.DEFAULT_PRIORITY,
                        Message.DEFAULT_TIME_TO_LIVE);
            } catch (Exception exception) {
                LOGGER.error("Sending error message failed [{}]", exception);
            }
        }
    }

    private void responseProcess(String messageId, Message message) throws Exception {
        AmqpMessageProducer _produce = null;
        for (AmqpMessageProducer producer : ComponentEnv.getEnv().getMessageProducers()) {
            LOGGER.debug("Response process [{}][{}]", producer.getMessageId(), messageId);
            if (producer.getMessageId().equals(messageId)) {
                LOGGER.debug("Message must process by [{}]", messageId);
                _produce = producer;
                _produce.setPayload(message);
                break;
            }
        }
        if (_produce != null)
            new MessageReceiveProcess(_produce).start();
    }

    private JbiExceptionEntity getJbiExceptionEntity(Object obj) throws Exception {
        JbiExceptionEntity entity = new JbiExceptionEntity();
        Method method = obj.getClass().getMethod("getUserTitle");
        entity.setUserTitle((String) method.invoke(obj));
        method = obj.getClass().getMethod("getMessage");
        entity.setMessage((String) method.invoke(obj));
        method = obj.getClass().getMethod("getUserMessage");
        entity.setUserMessage((String) method.invoke(obj));
        method = obj.getClass().getMethod("getCode");
        entity.setCode((int) method.invoke(obj));
        method = obj.getClass().getMethod("getSubCode");
        entity.setSubCode((int) method.invoke(obj));
        return entity;
    }
}
