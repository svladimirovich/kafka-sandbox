# Because of some unknown bug:
#   {error_code: 500, message: "Must configure one of topics or topics.regex"}
# I had to specify both "topic" & "topics" properties.

curl -X PUT \
  /api/kafka-connect/connectors/source-twitter-distributed/config \
  -H 'Content-Type: application/json' \
  -H 'Accept: application/json' \
  -d '{
  "connector.class": "com.eneco.trading.kafka.connect.twitter.TwitterSourceConnector",
  "topic": "demo-3-twitter",
  "topics": "demo-3-twitter",
  "tasks.max": "1",
  "track.terms": "starcraft,tekken",
  "language": "en",
  "key.converter.schemas.enable": "true",
  "value.converter.schemas.enable": "true",
  "value.converter": "org.apache.kafka.connect.json.JsonConverter",
  "key.converter": "org.apache.kafka.connect.json.JsonConverter",
  "twitter.consumerkey": "???????????????????",
  "twitter.consumersecret": "??????????????????????????????",
  "twitter.token": "????????????????????????????????",
  "twitter.secret": "???????????????"
}'