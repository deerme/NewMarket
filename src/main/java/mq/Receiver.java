package mq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.jms.*;

/**
 * Created by PBanasiak on 3/21/2016.
 */

public class Receiver implements ExceptionListener {
    private String queueName;
    private String serverName;
    private ActiveMQConnectionFactory connectionFactory;
    private Connection connection;
    private Session session;
    private Destination destination;
    private MessageConsumer consumer;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public Receiver(String queueName, String serverName) {
        this.queueName = queueName;
        this.serverName = serverName;
    }

    public void createConnection() throws JMSException {
        connectionFactory = new ActiveMQConnectionFactory(serverName);
        connection = connectionFactory.createConnection();
        connection.start();
        connection.setExceptionListener(this);

        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        destination = session.createQueue(queueName);
        consumer = session.createConsumer(destination);
    }

    public String getMessage() throws JMSException {
        Message message = consumer.receive();
        logger.trace("in getMessage");

        while (true) {
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                String text = textMessage.getText();
                logger.info("Received message: " + text);

                return text;
            }
        }
    }

    public void closeConnection() {
        try {
            consumer.close();
        } catch (JMSException e) {
            logger.warn("MessageConsumer with MQ wasn`t close", e);
        } finally {
            try {
                session.close();
            } catch (JMSException e) {
                logger.warn("Session with MQ wasn`t close", e);
            } finally {
                try {
                    connection.close();
                } catch (JMSException e) {
                    logger.warn("Connection with MQ wasn`t close", e);
                }
            }
        }
    }

    public void onException(JMSException e) {
        logger.error("JMSException",e);
    }
}