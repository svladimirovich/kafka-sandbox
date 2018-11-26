FROM ubuntu

# update package information
RUN apt update

# install Java 8
RUN apt --yes install openjdk-8-jdk

# install kafka
RUN apt --yes install curl
RUN curl -O http://mirror.linux-ia64.org/apache/kafka/2.0.1/kafka_2.12-2.0.1.tgz
RUN tar -xvf kafka_2.12-2.0.1.tgz
RUN rm kafka_2.12-2.0.1.tgz
RUN echo "export PATH=/kafka_2.12-2.0.1/bin:$PATH" >> ~/.bashrc

# as kafka course guy did for some reason, place kafka data files to a special folder
RUN mkdir /kafka_2.12-2.0.1/data
RUN mkdir /kafka_2.12-2.0.1/data/kafka
RUN sed -i -E 's/(log.dirs=)([^\n]*)/\1\/kafka_2.12-2.0.1\/data\/kafka/g' /kafka_2.12-2.0.1/config/server.properties

# set zookeeper host in kafka config
ARG ZOOKEEPER_HOST=zookeeper
RUN sed -i -E 's/(zookeeper\.connect=)([^\n]*)/\1'$ZOOKEEPER_HOST':2181/g' /kafka_2.12-2.0.1/config/server.properties

ARG KAFKA_ADVERTISED_HOST=kafka
# #advertised.listeners=PLAINTEXT://your.host.name:9092
RUN sed -i -E 's/#(advertised\.listeners=PLAINTEXT:\/\/)([^:]*)/\1'$KAFKA_ADVERTISED_HOST'/g' /kafka_2.12-2.0.1/config/server.properties

# kafka --broker-list
EXPOSE 9092

CMD ["kafka_2.12-2.0.1/bin/kafka-server-start.sh", "/kafka_2.12-2.0.1/config/server.properties"]