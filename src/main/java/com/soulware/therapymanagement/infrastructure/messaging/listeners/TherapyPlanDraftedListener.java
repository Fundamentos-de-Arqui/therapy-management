package com.soulware.therapymanagement.infrastructure.messaging.listeners;

import com.soulware.therapymanagement.application.commands.CreateTherapyPlanCommand;
import com.soulware.therapymanagement.application.commands.ScheduleEntryCommand;
import com.soulware.therapymanagement.application.services.commands.TherapyPlanCommandService;
import com.soulware.therapymanagement.infrastructure.messaging.dto.TherapyPlanDraftedMessage;

import jakarta.enterprise.inject.spi.CDI;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import javax.jms.*;
import java.time.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.activemq.ActiveMQConnectionFactory;

@WebListener
public class TherapyPlanDraftedListener implements ServletContextListener {

    private static final Logger logger = Logger.getLogger(TherapyPlanDraftedListener.class.getName());
    private static final String INPUT_QUEUE = "THERAPY_PLAN_DRAFTED";
    private static final String BROKER_URL = "tcp://localhost:61616";

    private volatile boolean running = true;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("=== Initializing TherapyPlanDraftedListener ===");

        Thread listenerThread = new Thread(this::runListener, "TherapyPlanDraftedListener-Thread");
        listenerThread.start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("=== Stopping TherapyPlanDraftedListener ===");
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

        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(
                "admin",
                "admin",
                BROKER_URL);

        try (
                Connection connection = factory.createConnection();
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE)
        ) {
            Queue queue = session.createQueue(INPUT_QUEUE);
            MessageConsumer consumer = session.createConsumer(queue);

            connection.start();

            logger.info("Listening on queue: " + INPUT_QUEUE);

            while (running) {
                Message message = consumer.receive(2000);

                if (message == null) {
                    continue;
                }

                handleMessage(message);
            }
        }
    }

    private void handleMessage(Message message) {
        try {
            String json = extractJson(message);
            if (json == null) {
                logger.warning("Empty or unsupported message type.");
                return;
            }

            logger.info("Received Therapy Plan Drafted message: " + json);

            Jsonb jsonb = JsonbBuilder.create();
            TherapyPlanDraftedMessage request =
                    jsonb.fromJson(json, TherapyPlanDraftedMessage.class);

            CreateTherapyPlanCommand command = new CreateTherapyPlanCommand(
                    request.assignedTherapistId.value,
                    request.patientId.value,
                    request.legalResponsibleId.value,
                    mapSchedule(request.schedule)
            );

            TherapyPlanCommandService service =
                    CDI.current().select(TherapyPlanCommandService.class).get();

            service.create(command);

            logger.info("SUCCESS: Therapy Plan created with PATIENT ID = " + command.patientId());

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error processing JMS message", e);
        }
    }

    private String extractJson(Message message) throws Exception {
        if (message instanceof TextMessage textMessage) {
            return textMessage.getText();
        }

        if (message instanceof BytesMessage bytesMessage) {
            long len = bytesMessage.getBodyLength();
            if (len <= 0) return null;
            byte[] data = new byte[(int) len];
            bytesMessage.readBytes(data);
            return new String(data, "UTF-8");
        }

        return null;
    }

    private static List<ScheduleEntryCommand> mapSchedule(TherapyPlanDraftedMessage.ScheduleDTO dto) {
        if (dto == null || dto.schedule == null) return List.of();

        return dto.schedule.entrySet().stream()
                .map(e -> new ScheduleEntryCommand(
                        e.getKey().toUpperCase(),
                        toZoned(e.getValue().start),
                        toZoned(e.getValue().end)
                ))
                .toList();
    }

    private static ZonedDateTime toZoned(String time) {
        return  ZonedDateTime.parse(time);
    }

    private void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }
}
