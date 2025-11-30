package com.soulware.therapymanagement.infrastructure.messaging.listeners;

import com.soulware.therapymanagement.application.commands.UpdateSessionStatusCommand;
import com.soulware.therapymanagement.application.services.commands.SessionCommandService;
import com.soulware.therapymanagement.infrastructure.messaging.dto.UpdatedSessionResource;
import com.soulware.therapymanagement.infrastructure.persistence.jpa.entities.TherapyScheduleEntryEntity;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.enterprise.inject.spi.CDI;

import javax.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

import java.util.logging.Level;
import java.util.logging.Logger;

@WebListener
public class UpdateSessionStatusListener implements ServletContextListener {

    private static final Logger logger = Logger.getLogger(UpdateSessionStatusListener.class.getName());

    private static final String INPUT_QUEUE = "session_updateStatus";
    private static final String OUTPUT_QUEUE = "session_statusUpdated";
    private static final String BROKER_URL = "tcp://localhost:61616";

    private volatile boolean running = true;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("=== Starting UpdateSessionStatusListener ===");
        Thread listener = new Thread(this::runListener, "UpdateSessionStatusListener-Thread");
        listener.start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("=== Stopping UpdateSessionStatusListener ===");
        running = false;
    }

    private void runListener() {
        while (running) {
            try {
                listenOnce();
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Listener crashed. Retrying in 5 seconds...", e);
                sleep(5000);
            }
        }
    }

    private void listenOnce() throws Exception {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("admin", "admin", BROKER_URL);

        try (
                Connection connection = factory.createConnection();
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE)
        ) {
            Queue in = session.createQueue(INPUT_QUEUE);
            Queue out = session.createQueue(OUTPUT_QUEUE);

            MessageConsumer consumer = session.createConsumer(in);
            MessageProducer producer = session.createProducer(out);

            connection.start();
            logger.info("Listening on queue: " + INPUT_QUEUE);

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
            UpdateSessionStatusMessage req = jsonb.fromJson(json, UpdateSessionStatusMessage.class);

            SessionCommandService service = CDI.current().select(SessionCommandService.class).get();

            logger.info("Received update session status request. id=" + req.id + ", newStatus=" + req.status);

            TherapyScheduleEntryEntity updatedEntry = service.updateSessionStatus(new UpdateSessionStatusCommand(req.id, req.status));

            UpdatedSessionResource entry = new UpdatedSessionResource(
                    updatedEntry.getEntryId(),
                    updatedEntry.getPlanId(),
                    updatedEntry.getStatus().getName(),
                    updatedEntry.getDay().toUpperCase(),
                    updatedEntry.getStartTime(),
                    updatedEntry.getEndTime()
            );

            String respJson = jsonb.toJson(entry);
            producer.send(session.createTextMessage(respJson));

            logger.info("Updated session status and sent response to " + OUTPUT_QUEUE);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in UpdateSessionStatusListener", e);
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

    public static class UpdateSessionStatusMessage {
        public Long id;
        public String status;
    }
}
