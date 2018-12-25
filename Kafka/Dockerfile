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

# set zookeeper host in kafka config
ARG ZOOKEEPER_HOST=zookeeper
RUN sed -i -E 's/(zookeeper\.connect=)([^\n]*)/\1'$ZOOKEEPER_HOST':2181/g' /kafka_2.12-2.0.1/config/server.properties

ARG KAFKA_ADVERTISED_HOST=kafka
# #advertised.listeners=PLAINTEXT://your.host.name:9092
RUN sed -i -E 's/#(advertised\.listeners=PLAINTEXT:\/\/)([^:]*)/\1'$KAFKA_ADVERTISED_HOST'/g' /kafka_2.12-2.0.1/config/server.properties

# kafka --broker-list
EXPOSE 9092

CMD ["kafka_2.12-2.0.1/bin/kafka-server-start.sh", "/kafka_2.12-2.0.1/config/server.properties"]