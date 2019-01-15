FROM maven:alpine AS buildSource
COPY . /build
WORKDIR /build
RUN mvn clean package

FROM java

COPY --from=buildSource /build/target/favorite-color-1.0-jar-with-dependencies.jar /target/kafka-streams-app.jar

CMD ["java", "-jar", "/target/kafka-streams-app.jar"]

