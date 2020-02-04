package org.service.common.message;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.service.common.message.kafka.KafkaMessageConsumer;
import org.service.common.message.kafka.KafkaMessageProducer;
import org.service.common.translator.JsonTranslator;

public class MessageConsumer_UnitTest {

    @Test
    void create_kafka() {
        // Setup
        MessageConsumerConfig config = new MessageConsumerConfig();
        config.engine = KafkaMessageProducer.NAME;

        @SuppressWarnings("unchecked")
        JsonTranslator<String, JsonMessage> translator = Mockito.mock(JsonTranslator.class);

        //Execute
        MessageConsumer<String, JsonMessage> subject = MessageConsumer.create(config, translator);

        // Validate
        assertNotNull(subject);
        KafkaMessageConsumer<String, JsonMessage> kafka = (KafkaMessageConsumer<String, JsonMessage>) subject;
        assertSame(config, kafka.config);
        assertSame(translator, kafka.translator);
        assertNotNull(kafka.mapper);
        assertNull(kafka.messageClass);
    }

    @Test
    void create_unknown() {
        // Setup
        MessageConsumerConfig config = new MessageConsumerConfig();
        config.engine = "unknown";

        @SuppressWarnings("unchecked")
        JsonTranslator<String, JsonMessage> translator = Mockito.mock(JsonTranslator.class);

        //Execute
        RuntimeException error = assertThrows(RuntimeException.class, () -> MessageConsumer.create(config, translator));
        assertTrue(error.getMessage().contains("Unsupported message consumer engine: unknown"));
    }
}
