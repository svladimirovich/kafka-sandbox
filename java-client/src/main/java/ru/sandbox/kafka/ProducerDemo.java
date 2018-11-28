package ru.sandbox.kafka;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import ru.sandbox.generator.IGeneratedMessage;
import ru.sandbox.generator.MessageGenerator;

import java.util.Properties;

public class ProducerDemo {
    public static void main(String[] args) {

        // create Producer properties
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // create Producer
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);

        MessageGenerator generator = new MessageGenerator();
        IGeneratedMessage message = generator.generateMessage();

        // create a message
        ProducerRecord<String, String> record = new ProducerRecord<String, String>("partitioned-topic", message.toJsonString());

        // send data
        producer.send(record, new Callback() {
            public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                System.out.println(
                        String.format(
                                "Received new metadata!\nTopic: %s\nPartition: %s\nOffset: %s\nTimestamp: %s",
                                recordMetadata.topic(),
                                recordMetadata.partition(),
                                recordMetadata.offset(),
                                recordMetadata.timestamp()
                        )
                );
            }
        });
        producer.flush();
        producer.close();


    }
}
