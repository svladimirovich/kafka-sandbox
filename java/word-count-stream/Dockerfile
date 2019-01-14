FROM maven:alpine AS buildSource
COPY . /build
WORKDIR /build
RUN mvn clean package

FROM java 
#:8-alpine
# Can't use alpine because kafka streams java app throws a log of missing lib errors

# To solve Java Issue on alpine image
# java.lang.UnsatisfiedLinkError: Error loading shared library libstdc++.so.6: No such file or directory
# RUN apk add --no-cache libstdc++

# java.lang.UnsatisfiedLinkError:
# Error loading shared library ld-linux-x86-64.so.2: No such file or directory
# RUN apk add --no-cache libc6-compat

COPY --from=buildSource /build/target/word-count-1.0-jar-with-dependencies.jar /target/kafka-streams-app.jar

CMD ["java", "-jar", "/target/kafka-streams-app.jar"]

