package mq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.jms.*;

/**
 * Created by PBanasiak on 3/21/2016.
 */
public class Sender {
    private String queueName;
    private String serverName;
    private ActiveMQConnectionFactory connectionFactory;
    private Connection connection;
    private Session session;
    private Destination destination;
    private MessageProducer messageProducer;
    private static final Logger logger = LoggerFactory.getLogger(Sender.class);

    public Sender(String queueName,String serverName) throws JMSException {
        this.queueName = queueName;
        this.serverName = serverName;
    }

    public void createConnection() throws JMSException {
        connectionFactory = new ActiveMQConnectionFactory(serverName);
        connection = connectionFactory.createConnection();
        connection.start();

        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        destination = session.createQueue(queueName);

        messageProducer = session.createProducer(destination);
        messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
    }

    public void sendMessage(String text) throws JMSException {
        TextMessage message = session.createTextMessage(text);
        messageProducer.send(message);
    }

    public void closeConnection(){
        try{
            session.close();
        }
        catch (JMSException e1) {
            logger.warn("Session can`t be closed",e1);
        }
        finally {
            try {
                connection.close();
            }
            catch(JMSException e2){
                logger.warn("Connection can`t be closed ",e2);
            }
        }
    }
    public void initSender() throws JMSException {
        this.createConnection ();
    }
}




