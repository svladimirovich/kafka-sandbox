package ru.sandbox.kafka;
import com.google.gson.Gson;
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
        this.emitter = Observable.interval(100, TimeUnit.MILLISECONDS).concatMap(x -> {
            return Observable.just(x)
                    .delay((long)(Math.random() * 2900), TimeUnit.MILLISECONDS)
                    .timeInterval().map(tick -> {
                        return new MessageGenerator().generateMessage();
                    });
        });
    }

    public void run() {
        // this.emitter.subscribeOn
        // this.emitter.observeOn
        this.emitter.blockingSubscribe(message -> {
            //System.out.println(new Gson().toJson(message));
            pushToKafka(message);
        });
    }

    public void pushToKafka(IGeneratedMessage message) {
        final String kafkaHost = "127.0.0.1:9092";
        final String kafkaTopic = "partitioned-topic";
        // create Producer properties
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaHost);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // create Producer
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);

        // create a message
        ProducerRecord<String, String> record = new ProducerRecord<String, String>(kafkaTopic, message.toJsonString());
        // send data
        producer.send(record, (recordMetadata, e) -> {
            if(e == null) {
                System.out.println(
                    String.format(
                            "Received new metadata!\nTopic: %s\nPartition: %s\nOffset: %s\nTimestamp: %s",
                            recordMetadata.topic(),
                            recordMetadata.partition(),
                            recordMetadata.offset(),
                            recordMetadata.timestamp()
                    )
                );
            } else {
                // Error
                System.out.println("Error while sending message to Kafka! " + e);
            }
        });
        producer.flush();
        producer.close();
    }
}
