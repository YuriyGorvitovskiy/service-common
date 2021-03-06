package org.service.common.message.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.service.common.message.JsonMessage;
import org.service.common.message.MessageProducer;
import org.service.common.message.MessageProducerConfig;
import org.service.common.translator.JsonTranslator;

import com.fasterxml.jackson.databind.ObjectMapper;

public class KafkaMessageProducer<M, J extends JsonMessage> implements MessageProducer<M, J> {

    public static final String NAME        = "KAFKA";
    static final String        MESSAGE_KEY = "JSON";

    public final MessageProducerConfig         config;
    public final JsonTranslator<M, J>          translator;
    public final ObjectMapper                  mapper;
    public final KafkaProducer<String, byte[]> producer;

    long lastOffset = -1;

    public KafkaMessageProducer(MessageProducerConfig config, JsonTranslator<M, J> translator) {
        this.config = config;
        this.translator = translator;

        this.mapper = new ObjectMapper();
        // No implicit nulls
        // this.mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        this.producer = new KafkaProducer<>(config.properties);
    }

    @Override
    public void post(M notify) throws Exception {
        J json = translator.toJson(notify);
        byte[] bytes = mapper.writeValueAsBytes(json);
        ProducerRecord<String, byte[]> record = new ProducerRecord<>(config.topic, MESSAGE_KEY, bytes);
        RecordMetadata meta = producer.send(record).get();
        lastOffset = meta.offset();
        producer.flush();
    }

    long getLastOffset() {
        return lastOffset;
    }
}
