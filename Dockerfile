FROM anapsix/alpine-java:8_jdk

RUN apk add --no-cache git nodejs \
&& git clone https://github.com/Wrongsides/cherrypickor.git \
&& cd cherrypickor && ./gradlew build \
&& cp cherrypickor-server/build/libs/cherrypickor-server.jar /cherrypickor.jar \
&& cd .. && rm -rf cherrypickor && apk del git nodejs

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","-Dspring.profiles.active=production","/cherrypickor.jar"]
EXPOSE 9000