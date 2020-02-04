package org.service.common.message;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.service.common.message.kafka.KafkaMessageProducer;
import org.service.common.translator.JsonTranslator;

public class MessageProducer_UnitTest {

    @Test
    void create_kafka() {
        // Setup
        MessageProducerConfig config = new MessageProducerConfig();
        config.engine = KafkaMessageProducer.NAME;

        @SuppressWarnings("unchecked")
        JsonTranslator<String, JsonMessage> translator = Mockito.mock(JsonTranslator.class);

        //Execute
        MessageProducer<String, JsonMessage> subject = MessageProducer.create(config, translator);

        // Validate
        assertNotNull(subject);
        KafkaMessageProducer<String, JsonMessage> kafka = (KafkaMessageProducer<String, JsonMessage>) subject;
        assertSame(config, kafka.config);
        assertSame(translator, kafka.translator);
        assertNotNull(kafka.mapper);
        assertNotNull(kafka.producer);
    }

    @Test
    void create_unknown() {
        // Setup
        MessageProducerConfig config = new MessageProducerConfig();
        config.engine = "unknown";

        @SuppressWarnings("unchecked")
        JsonTranslator<String, JsonMessage> translator = Mockito.mock(JsonTranslator.class);

        //Execute
        RuntimeException error = assertThrows(RuntimeException.class, () -> MessageProducer.create(config, translator));
        assertTrue(error.getMessage().contains("Unsupported message producer engine: unknown"));
    }
}
