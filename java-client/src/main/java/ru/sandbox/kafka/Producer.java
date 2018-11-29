package ru.sandbox.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class Producer {
    private KafkaProducer<String, String> producer;

    public Producer(Properties properties) {
        // create Producer
        this.producer = new KafkaProducer<String, String>(properties);
    }

    public void push(String kafkaTopic, String message) {
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
