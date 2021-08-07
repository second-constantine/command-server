This is a command server simple implementation. **Don't use it for illegal actions.**

# Requirements
gradle 7.1, jdk 11, screen, bash, curl

# Server
1. build `gradle build`
2. Run `./build/libs/command-server-1.0-SNAPSHOT.jar` 
3. Open [swagger ui](http://localhost:8889) 

### Get commands
```bash
curl -X 'GET' \
  'http://localhost:8889/api/get-commands/123token123' \
  -H 'accept: */*'
```

### Send command
```bash
curl -X 'POST' \
  'http://localhost:8889/api/send-command/123token123' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d 'uname -a'
```

> Important: run the `get-commands` method before the `send-command` method

# Client
Run command agent in background
```bash
screen -dmS command-agent ./command-agent.sh
```
