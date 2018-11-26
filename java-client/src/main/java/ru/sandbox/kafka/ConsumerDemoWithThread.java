package ru.sandbox.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class ConsumerDemoWithThread {
    private Logger logger = LoggerFactory.getLogger(ConsumerDemoWithThread.class);

    public static void main(String[] args) {
        new ConsumerDemoWithThread().run();
    }

    private ConsumerDemoWithThread() {
    }

    private void run() {
        // Create consumer configs
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "my-group-id-0");
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // create consumer
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties);
        CountDownLatch latch = new CountDownLatch(1);
        Runnable kafkaConsumer = new KafkaConsumerThread(latch, properties);

        Thread myThread = new Thread(kafkaConsumer);
        myThread.start();

        // add shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Caught shutdown hook");
            ((KafkaConsumerThread) kafkaConsumer).shutdown();
            try {
                latch.await();
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }));

        try {
            latch.await();
        } catch(InterruptedException e) {
            logger.error("Application error:", e);
        } finally {
            logger.info("Application is closing");
        }
    }

    public class KafkaConsumerThread implements Runnable {
        private CountDownLatch latch;
        private KafkaConsumer<String, String> consumer;
        private Logger logger = LoggerFactory.getLogger(KafkaConsumerThread.class);

        public KafkaConsumerThread(CountDownLatch latch, Properties properties) {
            super();
            this.latch = latch;
            this.consumer = new KafkaConsumer<String, String>(properties);
        }

        @Override
        public void run() {

            try {
                // subscribe consumer to our topic
                consumer.subscribe(Arrays.asList("partitioned-topic"));

                while(true) {
                    ConsumerRecords<String, String> messages = consumer.poll(Duration.ofMillis(100));

                    for(ConsumerRecord<String, String> record : messages) {
                        logger.info(String.format(
                                "Received Message! Key: %s Message: %s Partition: %s Offset: %s",
                                record.key(), record.value(), record.partition(), record.offset()));
                    }
                }
            } catch (WakeupException e) {
                logger.info("Received shutdown signal!");
            } finally {
                consumer.close();
                // tell our main code we're down with the consumer business
                latch.countDown();
            }
        }

        public void shutdown() {
            // the wakeup() method interrupts consumer.poll() with WakeupException
            consumer.wakeup();
        }
    }
}
