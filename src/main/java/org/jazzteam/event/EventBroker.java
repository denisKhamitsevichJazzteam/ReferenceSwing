package org.jazzteam.event;

import lombok.SneakyThrows;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.jazzteam.event.model.AppEvent;
import org.jazzteam.event.model.EventType;
import org.jazzteam.util.EventSerializer;

import javax.jms.*;
import java.util.UUID;

/**
 * EventBroker is responsible for publishing and subscribing to application events
 * via ActiveMQ.
 * <p>
 * It serializes events to JSON and sends them to a JMS topic, and also listens
 * for incoming events from the topic to dispatch them locally via {@link EventDispatcher}.
 * <p>
 * Each instance has a unique sender ID to avoid processing its own published events.
 * The broker URL, topic name, and JMS properties are currently hardcoded but can be
 * externalized if needed.
 */
public class EventBroker {
    private static final String BROKER_URL = "tcp://localhost:61616";
    private static final String TOPIC_NAME = "app.events";
    public static final String EVENT_TYPE = "eventType";
    public static final String SENDER_ID = "senderId";
    private static final String INSTANCE_ID = UUID.randomUUID().toString();

    private final Session session;
    private final MessageProducer producer;
    private final EventDispatcher dispatcher;


    @SneakyThrows
    public EventBroker(EventDispatcher dispatcher) {
        this.dispatcher = dispatcher;
        ConnectionFactory factory = new ActiveMQConnectionFactory(BROKER_URL);
        Connection connection = factory.createConnection();
        connection.start();

        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createTopic(TOPIC_NAME);
        producer = session.createProducer(destination);
    }

    @SneakyThrows
    public void publish(AppEvent event) {
        String json = EventSerializer.serialize(event);
        TextMessage message = session.createTextMessage(json);
        message.setStringProperty(EVENT_TYPE, event.getType().name());
        message.setStringProperty(SENDER_ID, INSTANCE_ID);
        producer.send(message);
    }

    @SneakyThrows
    public void subscribe() {
        Destination destination = session.createTopic(TOPIC_NAME);
        MessageConsumer consumer = session.createConsumer(destination);
        consumer.setMessageListener(msg -> {
            if (msg instanceof TextMessage) {
                TextMessage textMsg = (TextMessage) msg;
                try {
                    String senderId = textMsg.getStringProperty(SENDER_ID);
                    if (INSTANCE_ID.equals(senderId)) return;

                    String typeName = textMsg.getStringProperty(EVENT_TYPE);
                    EventType type = EventType.valueOf(typeName);
                    AppEvent event = EventSerializer.deserialize(textMsg.getText(), type);
                    dispatcher.dispatch(event);
                } catch (Exception e) {
                    throw new RuntimeException(
                            "Cannot process JMS message. MessageType: " + msg.getClass().getSimpleName(), e);
                }
            }
        });
    }
}
