package org.service.common.message;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;
import org.service.common.message.kafka.KafkaMessageConsumer;
import org.service.common.message.kafka.KafkaMessageProducer;
import org.service.common.translator.JsonTranslator;

public class MessageConsumer_UnitTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void create_kafka() {
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
    public void create_unknown() {
        // Setup
        MessageConsumerConfig config = new MessageConsumerConfig();
        config.engine = "unknown";

        @SuppressWarnings("unchecked")
        JsonTranslator<String, JsonMessage> translator = Mockito.mock(JsonTranslator.class);

        exception.expect(RuntimeException.class);
        exception.expectMessage("Unsupported message consumer engine: unknown");

        //Execute
        MessageConsumer.create(config, translator);
    }
}