package ru.sandbox.kafka;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerDemoWithCallback {
    public static void main(String[] args) {

        final Logger logger = LoggerFactory.getLogger(ProducerDemoWithCallback.class);

        // create Producer properties
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // create Producer
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);

        for(int index = 0; index < 10; index++) {
            // create a message
            ProducerRecord<String, String> message = new ProducerRecord<String, String>("partitioned-topic", String.format("message %d from Java!", index));

            // send data
            producer.send(message, new Callback() {
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    if (e != null) {
                        // error
                        logger.error("Error while producing", e);
                    } else {
                        // success
                        logger.info(
                                String.format(
                                        "Received new metadata!\nTopic: %s\nPartition: %s\nOffset: %s\nTimestamp: %s",
                                        recordMetadata.topic(),
                                        recordMetadata.partition(),
                                        recordMetadata.offset(),
                                        recordMetadata.timestamp()
                                )
                        );
                    }
                }
            });
        }
        producer.flush();
        producer.close();


    }
}
