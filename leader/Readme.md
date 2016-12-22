
To query the game:

```
curl http://localhost:8080/score
```

To post a score to the game:

```
curl -X PUT -H "Content-Type: application/json" -d '1' http://localhost:8080/home/score
curl -X PUT -H "Content-Type: application/json" -d '1' http://localhost:8080/visitor/score
```
