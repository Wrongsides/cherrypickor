FROM anapsix/alpine-java:8_jdk

RUN apk add --no-cache git \
&& git clone https://github.com/Wrongsides/cherrypickor.git \
&& cd cherrypickor && ./gradlew build \
&& cp cherrypickor-server/build/libs/cherrypickor-server.jar /app.jar \
&& cd .. && rm -rf cherrypickor && apk del git

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","-Dspring.profiles.active=production","/app.jar"]
EXPOSE 9000