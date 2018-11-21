package org.service.common.message;

import org.service.common.message.kafka.KafkaMessageProducer;
import org.service.common.translator.JsonTranslator;

public interface MessageProducer<M, J extends JsonMessage> {

    public static <M, J extends JsonMessage> MessageProducer<M, J> create(MessageProducerConfig config,
                                                                          JsonTranslator<M, J> translator) {
        if (KafkaMessageProducer.NAME.equals(config.engine)) {
            return new KafkaMessageProducer<M, J>(config, translator);
        }
        throw new RuntimeException("Unsupported message producer engine: " + config.engine);
    }

    public void post(M notify) throws Exception;

}
