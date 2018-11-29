package ru.sandbox.kafka;
import io.reactivex.*;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import ru.sandbox.generator.IGeneratedMessage;
import ru.sandbox.generator.MessageGenerator;


public class RandomMessageEmitter {
    private Observable<IGeneratedMessage> emitter;
    public static void main(String[] args) {
        RandomMessageEmitter messageEmitter = new RandomMessageEmitter();
        messageEmitter.run();
    }

    public RandomMessageEmitter() {
        this.emitter = Observable.interval(10, TimeUnit.MILLISECONDS).concatMap(x -> {
            return Observable.just(x)
                    .delay((long)(Math.random() * 340), TimeUnit.MILLISECONDS)
                    .timeInterval().map(tick -> {
                        return new MessageGenerator().generateMessage();
                    });
        });
    }

    public void run() {
        // create Producer properties
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // ******************************** safe Producer settings **************************************
        // ENABLE_IDEMPOTENCE_CONFIG available since Kafka 0.11
        // avoids duplicate messages on retries in case if ack response failed
        properties.setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
        properties.setProperty(ProducerConfig.ACKS_CONFIG, "all");
        properties.setProperty(ProducerConfig.RETRIES_CONFIG, Integer.toString(Integer.MAX_VALUE));
        // IDEMPOTENCE works with 1 MAX request per connection
        // but since Kafka 1.1, more than 1 can be used safely and will maintain order of messages
        properties.setProperty(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "5");
        // **********************************************************************************************

        Producer kafkaProducer = new Producer(properties);
        this.emitter.blockingSubscribe(message -> {
            kafkaProducer.push("partitioned-topic", message.toJsonString());
            kafkaProducer.flush();
        }, throwable -> {
            System.out.println("Error occurred: " + throwable);
            kafkaProducer.close();
        });
    }
}
