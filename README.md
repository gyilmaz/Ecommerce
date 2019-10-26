**Build**

Build the application as a Docker image using:

`mvn clean install`

**Dockerization**

`$ mvn install dockerfile:build`

**Run**

Run the included docker-compose file with:

`docker-compose up`

Generate logs
Call end points to create logs

Go to splunk `{{host}}:8000` to see the logs. 


Sample Splunk Query
`host=splunkforwarder| regex message=(?.(?=Log.)) | stats latest(trace) as trace latest(class) as class latest(message) as message by thread
`

Run Jenkins on port 8090

`docker-compose -f docker-composedev.yml up -d`

Go to `{{host}}:8090` to build a pipeline