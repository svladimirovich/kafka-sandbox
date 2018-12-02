package ru.sandbox.kafka;

import org.apache.http.HttpHost;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class ElasticSearchConsumer {

    private RestHighLevelClient elasticSearch;

    public static void main(String[] args) throws IOException {

        ElasticSearchConsumer consumer = new ElasticSearchConsumer();
        KafkaConsumer<String, String> kafkaConsumer = ElasticSearchConsumer.createConsumer("partitioned-topic");

        while(true) {
            ConsumerRecords<String, String> messages = kafkaConsumer.poll(Duration.ofMillis(100));

            for(ConsumerRecord<String, String> record : messages) {
                System.out.println(String.format(
                        "Received Message! Key: %s Message: %s Partition: %s Offset: %s",
                        record.key(), record.value(), record.partition(), record.offset()));

                // Inserting entry to ElasticSearch for each message in the kafka topic
                // unique kafka message id required for the insert to elastic search to be idempotent
                String kafkaId = String.format("%s_%s_%d", record.topic(), record.partition(), record.offset());
                IndexRequest indexRequest = new IndexRequest("lorem", "message", kafkaId).source(record.value(), XContentType.JSON);
                IndexResponse response = consumer.elasticSearch.index(indexRequest, RequestOptions.DEFAULT);
                String id = response.getId();
                System.out.println("Received Id from elasticsearch: " + id);

            }
        }

        // consumer.elasticSearch.close();


    }

    public ElasticSearchConsumer() {

        RestClientBuilder builder = RestClient.builder(new HttpHost("localhost", 9200, "http"));

        this.elasticSearch = new RestHighLevelClient(builder);


    }

    public static KafkaConsumer<String, String> createConsumer(String topic) {
        Logger logger = LoggerFactory.getLogger(ConsumerDemo.class);

        // Create consumer configs
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "kafka-demo-elasticsearch");
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // create consumer
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties);
        // subscribe consumer to our topic
        consumer.subscribe(Arrays.asList(topic));

        return consumer;
    }
}
