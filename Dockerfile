FROM java:8
VOLUME /tmp
RUN apt-get install -y --no-install-recommends git-core \
 && git clone https://github.com/Wrongsides/cherrypickor.git

WORKDIR /cherrypickor

RUN ./gradlew build

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","-Dspring.profiles.active=production","build/libs/cherrypickor-0.0.1-SNAPSHOT.jar"]
EXPOSE 9000