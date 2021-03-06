package org.service.common.message;

import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.service.common.message.kafka.KafkaMessageConsumer;

public class MessageConsumerConfig {

    public String engine = KafkaMessageConsumer.NAME;

    public String topic = "state-patch";

    public int partition = 0;

    @SuppressWarnings("serial")
    public Properties properties = new Properties() {
        {
            this.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
            this.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
            this.put(ConsumerConfig.GROUP_ID_CONFIG, "state-patch-sql");
            this.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            this.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class.getName());
        }
    };
}
