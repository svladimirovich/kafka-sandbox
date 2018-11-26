FROM ubuntu

# update package information
RUN apt update

# install Java 8
RUN apt --yes install openjdk-8-jdk

# install netstat and stuff
# RUN apt install net-tools

# install kafka
RUN apt --yes install curl
#WORKDIR /kafka
RUN curl -O http://mirror.linux-ia64.org/apache/kafka/2.0.1/kafka_2.12-2.0.1.tgz
RUN tar -xvf kafka_2.12-2.0.1.tgz
RUN rm kafka_2.12-2.0.1.tgz
RUN echo "export PATH=/kafka_2.12-2.0.1/bin:$PATH" >> ~/.bashrc

# as kafka course guy did for some reason, place zookeeper data files to a special folder
RUN mkdir /kafka_2.12-2.0.1/data
RUN mkdir /kafka_2.12-2.0.1/data/zookeeper
#RUN mkdir /kafka_2.12-2.0.1/data/kafka
RUN sed -i -E 's/(dataDir=)([^\n]*)/\1\/kafka_2.12-2.0.1\/data\/zookeeper/g' /kafka_2.12-2.0.1/config/zookeeper.properties

# Zookeeper port
EXPOSE 2181

CMD ["kafka_2.12-2.0.1/bin/zookeeper-server-start.sh", "/kafka_2.12-2.0.1/config/zookeeper.properties"]