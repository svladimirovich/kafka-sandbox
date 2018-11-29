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
