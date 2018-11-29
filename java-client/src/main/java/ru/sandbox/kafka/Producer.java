package ru.sandbox.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class Producer {
    final String kafkaHost = "127.0.0.1:9092";
    final String kafkaTopic = "partitioned-topic";

    private KafkaProducer<String, String> producer;

    public Producer() {
        // create Producer properties
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaHost);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        // create Producer
        this.producer = new KafkaProducer<String, String>(properties);
    }

    public void push(String message) {
        // create a message
        ProducerRecord<String, String> record = new ProducerRecord<String, String>(kafkaTopic, message);
        // send data
        producer.send(record, (recordMetadata, e) -> {
            if(e == null) {
                System.out.println(
                        String.format(
                                "=========================================================================================================\n" +
                                "Received new metadata!\tTopic: %s\tPartition: %s\tOffset: %s\tTimestamp: %s\n" +
                                "Message: %s",
                                recordMetadata.topic(),
                                recordMetadata.partition(),
                                recordMetadata.offset(),
                                recordMetadata.timestamp(),
                                message
                        )
                );
            } else {
                // Error
                System.out.println("Error while sending message to Kafka! " + e);
            }
        });
    }

    public void flush() {
        producer.flush();
    }

    public void close() {
        producer.close();
    }
}
