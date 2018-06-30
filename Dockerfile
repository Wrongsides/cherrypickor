FROM anapsix/alpine-java:8_jdk

RUN apk add --no-cache git \
&& git clone https://github.com/Wrongsides/cherrypickor.git \
&& cd cherrypickor && ./gradlew build \
&& cp build/libs/cherrypickor-0.0.1-SNAPSHOT.jar /cherrypickor.jar \
&& cd .. && rm -rf cherrypickor && rm -rf ~/.gradle && apk del git

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","-Dspring.profiles.active=production","/cherrypickor.jar"]
EXPOSE 9000