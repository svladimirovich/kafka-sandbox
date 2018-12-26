package ru.sandbox.kafka;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerDemoKeys {
    public static void main(String[] args) {

        final Logger logger = LoggerFactory.getLogger(ProducerDemoKeys.class);

        // create Producer properties
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // create Producer
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);

        for(int index = 10; index < 20; index++) {
            // create a message
            String topic = "partitioned-topic";
            final String messageText = String.format("keyed message %d from Java!", index);
            String key = "id_" + (index % 3);

            final ProducerRecord<String, String> message = new ProducerRecord<String, String>(topic, key, messageText);

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
                                        "Received new metadata!\nTopic: %s\nMessage: %s\nPartition: %s\nOffset: %s\nTimestamp: %s",
                                        recordMetadata.topic(),
                                        messageText,
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
