package ru.sandbox.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;

public class ConsumerDemoAssignSeek {
    public static void main(String[] args) {
        String topic = "partitioned-topic";
        Logger logger = LoggerFactory.getLogger(ConsumerDemoAssignSeek.class);

        // Create consumer configs
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        //properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "my-group-id-0");
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // create consumer
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        // assign and seek are mostly used to replay data or fetch a specific message

        // assign
        TopicPartition partitionToReadFrom = new TopicPartition(topic, 0);
        long offset = 10;
        consumer.assign(Collections.singletonList(partitionToReadFrom));

        // seek
        consumer.seek(partitionToReadFrom, offset);

        // subscribe consumer to our topic
        // consumer.subscribe(Arrays.asList(topic));

        int numOfMessagesToRead = 5;


        while(numOfMessagesToRead > 0) {
            ConsumerRecords<String, String> messages = consumer.poll(Duration.ofMillis(100));

            for(ConsumerRecord<String, String> record : messages) {
                logger.info(String.format(
                        "Received Message! Key: %s Message: %s Partition: %s Offset: %s",
                        record.key(), record.value(), record.partition(), record.offset()));
                numOfMessagesToRead--;
            }
        }
    }
}
