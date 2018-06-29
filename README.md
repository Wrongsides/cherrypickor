Backend app for cherry picking the best asteroids to mine in a given belt or anomaly based on current market values.

API root: localhost:9000

To run locally against latest ESI: 
``` 
./gradlew build && java -jar -Dspring.profiles.active=production build/libs/cherrypickor-0.0.1-SNAPSHOT.jar
```

To deploy with docker:
```
./gradlew build
docker build -t wrongsides/cherrypickor .
docker run -d -p 9000:9000 --name cherrypickor wrongsides/cherrypickor
```

![cherrypickor](cherrypickor.jpg?raw=true)
