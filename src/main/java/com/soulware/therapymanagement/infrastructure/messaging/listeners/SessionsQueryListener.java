package com.soulware.therapymanagement.infrastructure.messaging.listeners;

import com.soulware.therapymanagement.application.queries.GetSessionsQuery;
import com.soulware.therapymanagement.application.services.queries.SessionQueryService;
import com.soulware.therapymanagement.infrastructure.messaging.dto.PagedResponseResource;
import com.soulware.therapymanagement.shared.infrastructure.SessionResourceAssembler;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

import jakarta.enterprise.inject.spi.CDI;

import javax.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebListener
public class SessionsQueryListener implements ServletContextListener {

    private static final Logger logger = Logger.getLogger(SessionsQueryListener.class.getName());

    private static final String INPUT_QUEUE = "scheduling_getSessions";
    private static final String OUTPUT_QUEUE = "profile_getSessions";
    private static final String BROKER_URL = "tcp://localhost:61616";

    private volatile boolean running = true;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("=== Starting SessionsQueryListener ===");

        Thread listener = new Thread(this::runListener, "SessionsQueryListener-Thread");
        listener.start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("=== Stopping SessionsQueryListener ===");
        running = false;
    }

    private void runListener() {
        while (running) {
            try {
                listenOnce();
            } catch (Exception e) {
                logger.log(Level.SEVERE, "SessionsQueryListener crashed. Retrying in 5 seconds...", e);
                sleep(5000);
            }
        }
    }

    private void listenOnce() throws Exception {

        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(
                "admin", "admin", BROKER_URL
        );

        try (
                Connection connection = factory.createConnection();
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE)
        ) {
            Queue in = session.createQueue(INPUT_QUEUE);
            Queue out = session.createQueue(OUTPUT_QUEUE);

            MessageConsumer consumer = session.createConsumer(in);
            MessageProducer producer = session.createProducer(out);

            connection.start();

            logger.info("Listening sessions on queue: " + INPUT_QUEUE);

            while (running) {
                Message message = consumer.receive(2000);
                if (message == null) continue;

                handleMessage(message, session, producer);
            }
        }
    }

    private void handleMessage(Message message, Session session, MessageProducer producer) {
        try {
            String json = extractJson(message);
            if (json == null) return;

            Jsonb jsonb = JsonbBuilder.create();
            GetSessionsMessage req = jsonb.fromJson(json, GetSessionsMessage.class);

            GetSessionsQuery query = new GetSessionsQuery(
                    req.therapistId,
                    req.responsibleLegalId,
                    req.patientId,
                    req.status,
                    req.page,
                    req.size
            );

            SessionQueryService service = CDI.current().select(SessionQueryService.class).get();
            var result = service.getSessions(query);

            List<?> items = result.getItems().stream()
                    .map(SessionResourceAssembler::toResource)
                    .toList();

            PagedResponseResource<?> response = new PagedResponseResource<>(
                    items,
                    result.getTotalItems(),
                    result.getTotalPages(),
                    result.getPage(),
                    result.getSize()
            );

            String respJson = jsonb.toJson(response);
            producer.send(session.createTextMessage(respJson));

            logger.info("Sent sessions response to " + OUTPUT_QUEUE);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in SessionsQueryListener", e);
        }
    }

    private String extractJson(Message message) throws Exception {
        if (message instanceof TextMessage tm) return tm.getText();

        if (message instanceof BytesMessage bm) {
            byte[] data = new byte[(int) bm.getBodyLength()];
            bm.readBytes(data);
            return new String(data);
        }

        return null;
    }

    private void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }

    public static class GetSessionsMessage {
        public Long therapistId;
        public Long responsibleLegalId;
        public Long patientId;
        public String status;
        public int page;
        public int size;
    }
}
