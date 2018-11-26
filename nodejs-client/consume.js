const kafka = require("kafka-node");
 
const client = new kafka.KafkaClient({ kafkaHost: "localhost:9092" });
 
const topics = [
    {
        topic: "my-balanced-topic"
    }
];
const options = {
    autoCommit: false,
    // encoding: "buffer",
    // autoCommitIntervalMs
    // fetchMaxBytes: 1024 * 1024,
    // fetchMaxWaitMs: 1000,
    // fetchMinBytes
    // fromOffset
    groupId: "my-group",
    // keyEncoding
};
 
//const consumer = new kafka.Consumer(client, topics, options);
const consumer = new kafka.HighLevelConsumer(new kafka.Client("localhost:2181"), topics, options);
 
consumer.on("message", function(message) {
 
    // Read string into a buffer.
    var buf = new Buffer(message.value, "binary"); 
    //var decodedMessage = JSON.parse(buf.toString());
    var messageText = buf.toString();
 
    // Events is a Sequelize Model Object. 
    console.log(`Received kafka mesage!: ${messageText} \n${JSON.stringify(message)}\n`);
});
 
consumer.on("error", function(err) {
    console.log("error", err);
});
 
process.on("SIGINT", function() {
    consumer.close(true, function() {
        process.exit();
    });
});