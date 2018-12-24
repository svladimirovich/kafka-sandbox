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

# list of host/port pairs to use for establishing the initial connection to the Kafka cluster
ARG BOOTSTRAP_SERVERS_LIST=kafka:9092
RUN sed -i -E 's/(bootstrap\.servers=)([^\n]*)/\1'$BOOTSTRAP_SERVERS_LIST'/g' /kafka_2.12-2.0.1/config/connect-distributed.properties

ARG REST_PORT=8083
# #rest.port=8083
RUN sed -i -E 's/#(rest.port)=([^:]*)/\1='$REST_PORT'/g' /kafka_2.12-2.0.1/config/connect-distributed.properties

RUN mkdir connectors
# #plugin.path=
RUN sed -i -E 's/#(plugin\.path)=([^:]*)/\1=\/connectors/g' /kafka_2.12-2.0.1/config/connect-distributed.properties

# ARG ADVERTISED_HOST_NAME=connect
# # #rest.advertised.host.name=
# RUN sed -i -E 's/#(rest\.advertised\.host\.name)=([^:]*)/\1='$ADVERTISED_HOST_NAME'/g' /kafka_2.12-2.0.1/config/connect-distributed.properties

COPY ./entrypoint.sh /
ENTRYPOINT ["/entrypoint.sh"]

# expose kafka connect REST API port
EXPOSE 8083

WORKDIR /kafka_2.12-2.0.1
CMD ["bin/connect-distributed.sh", "config/connect-distributed.properties"]