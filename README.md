##Build
Build the application as a Docker image using:
```
mvn clean install
```

##Dockerization

```
$ mvn install dockerfile:build
```

##Run
Run the included docker-compose file with:
```
docker-compose up
```

##Generate logs
Call end points to create logs

Go to splunk ```{{host}}:8000 ```to see the logs. 

##Splunk
Sample Splunk Query
```host=splunkforwarder| regex message=(?.(?=Log.)) | stats latest(trace) as trace latest(class) as class latest(message)```
![](splunk.gif)
## Pipeline
Jenkins pipline is build for develop branch
Pipeline trigger 
![](jenkins.gif)