![build status](https://dockerbuildbadges.quelltext.eu/status.svg?organization=wrongsides&repository=cherrypickor)

An app for cherry picking the best asteroids to mine in a given belt or anomaly based on current market values.

API root: localhost:9000/api

To run locally against latest ESI: 
``` 
./gradlew build && java -jar -Dspring.profiles.active=production cherrypickor-server/build/libs/cherrypickor-server.jar
```

To deploy from docker hub:
```
docker run -d -p 9000:9000 --name cherrypickor wrongsides/cherrypickor
```

![cherrypickor](cherrypickor.png?raw=true)
