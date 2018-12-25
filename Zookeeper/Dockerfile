# download kafka on small alpine image with curl installed
FROM alpine AS kafkaFileDownload
RUN apk add curl
RUN curl -O http://mirror.linux-ia64.org/apache/kafka/2.0.1/kafka_2.12-2.0.1.tgz

# host kafka-connect on java 8 image running connect-distributed
FROM java:8-alpine

# seems like kafka *.sh scripts require bash (and ["/entrypoint.sh"] probably too)
RUN apk update
RUN apk add bash

COPY --from=kafkaFileDownload /kafka_2.12-2.0.1.tgz .
RUN tar -xvf kafka_2.12-2.0.1.tgz
RUN rm kafka_2.12-2.0.1.tgz
# add kafka/bin to PATH
RUN echo "export PATH=/kafka_2.12-2.0.1/bin:$PATH" >> ~/.bashrc

# Zookeeper port
EXPOSE 2181

CMD ["kafka_2.12-2.0.1/bin/zookeeper-server-start.sh", "/kafka_2.12-2.0.1/config/zookeeper.properties"]