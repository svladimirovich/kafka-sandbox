//const uuid = require("uuid");
const kafka = require("kafka-node");
const client = new kafka.KafkaClient({ kafkaHost: "127.0.0.1:9092" })
const producer = new kafka.Producer(client, { requireAcks: 1 });

producer.on("ready", () => {
    console.log("Kafka producer is connected and ready.");

    const keyedMessages = [
        new kafka.KeyedMessage("my-key-1","my message 1.7"),
        new kafka.KeyedMessage("my-key-2","my message 2.12"),
        new kafka.KeyedMessage("my-key-3","my message 3.3")
    ];

    const payload = [
        {
            topic: "my-topic",
            //messages: ['recieve this, Docker!', 'another message!'],
            messages: keyedMessages
            //partition: 0,
            //attributes: 2,
            //timestamp: Date.now()
        }
    ];

    producer.send(payload, (error, data) => {
        if(error) {
            console.log("Error on producer.send():", error)
        } else {
            console.log("sent data:", data)
        }
        client.close();
    })
    
});

producer.on("error", error => {
    console.log("Error occured!", error);
});

//producer.connect();