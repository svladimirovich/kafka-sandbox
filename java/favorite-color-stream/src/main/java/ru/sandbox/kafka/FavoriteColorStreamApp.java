package ru.sandbox.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;

import java.util.Properties;

/*

console commands to test this stuff:
------------------------------------

kafka-console-consumer.sh --bootstrap-server kafka:9092 --topic favorite-color-output --formatter kafka.too
ls.DefaultMessageFormatter --property print.key=true --property print.value=true property key.deserializer=org.apache
.kafka.common.serialization.StringDeserializer --property value.deserializer=org.apache.kafka.common.serialization.Lo
ngDeserializer --from-beginning

------------------------------------

kafka-console-producer.sh --broker-list kafka:9092 --topic favorite-color-input --property "parse.key=true"
 --property "key.separator=,"

 */


public class FavoriteColorStreamApp {
    public static void main(String[] args) {

        Properties properties = new Properties();
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "favorite-color");
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        StreamsBuilder builder = new StreamsBuilder();

        KStream<String, String> favColorsInput = builder.stream("favorite-color-input");

        favColorsInput
                .mapValues(value -> value.toLowerCase())
                .filter((key, value) -> value.matches("(?i)^(red|green|blue){1}$"))
                .to("favorite-color-filtered");

        KTable<String, String> favColorsTable = builder.table("favorite-color-filtered");

        KTable<String, Long> colorRatings = favColorsTable
                .groupBy((user_id, color) -> new KeyValue<>(color, color))
                .count(Materialized.as("ColorRatings"));

        colorRatings.toStream().to("favorite-color-output", Produced.with(Serdes.String(), Serdes.Long()));

        KafkaStreams streams = new KafkaStreams(builder.build(), properties);

        // Add a shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));

        streams.start();

        System.out.println(String.format("Printing the topology: %s", streams.toString()));

    }
}
