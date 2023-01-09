FROM openjdk:13-jdk-alpine
RUN mkdir -p /usr/src/kprime-cli
WORKDIR /
COPY ./target/kp.jar kprime-cli.jar
ENV KPRIME_HOME=$KPRIME_HOME
CMD ["java", "-jar", "kprime-cli.jar"]