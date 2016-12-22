To run a follower and specify a specific port:

```
mvn spring-boot:run -Drun.arguments=--server.port=9091
```

To query the state machine:

```
curl http://localhost:9091/score
```

To post a score to the log:

```
curl -X POST -H "Content-Type: application/json" -d '{"team": "visitor", "score": 2}' http://localhost:8080/appendScore
```
