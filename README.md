##Food Truck
Uses java as a programming language and mongodb as a persistence unit. There is also a scheduler that fetches
new data every 5 sec from data.sfgov.org. You can change those settings by updating appropriate property in the specific properties file. 

##Tech stack
- Spring boot and Spring data
- Java
- Gradle (build tool)
- Groovy (unit testing)
- Spock (unit testing)
- Docker
- MongoDB
- Hystrix (latency and fault tolerance library)
- Hystrix dashboard (for API monitoring)
- Swagger (for REST documentation)
- HATEOS (HAL implementation)

##Launch
You can launch project in  default(production), dev, test profile. Please note by default app starts on port 8080 and requires mongodb running on localhost
```./gradlew bootRun -Dspring.profiles.active=dev```

## Building docker image
```./gradlew clean build dockerBuildImage```

##REST documentation
```http://localhost:8080:8888/swagger-ui/``` .
If you are running the app locally , then please use
```http://LOCALHOST_OR_DOCKER_IP:PORT_ON_WHICH_APP_IS_RUNNING/swagger-ui.html``` .

Next in swagger gui you can visit search-controller, food-truck-controller and food-item-controller to read API documentation

## Interacting with API
If you don't want to open Swagger UI then execute
Search ```http://http://localhost:8080/api/search```
and follow the links

##Note
Most of the API's are paginated and uses HAL but in order to DEMONSTRATE that HAL and pagination is not the ONLY way, I have build few methods that return NON PAGINATED DATA.
However, I recommend and personally prefer paginated results.


##API Monitoring
```http://localhost:8080/hystrix/monitor?stream=http%3A%2F%2Flocalhost:8080%2Fhystrix.stream&title=Monitor%20API%20Calls``` .


##Health/Monitoring
If you are running it locally or inside local docker container then please change IP address and/or port as per your requirement.
```json
{
  "_links": {
    "self": {
      "href": "http://localhost:8080/actuator"
    },
    "trace": {
      "href": "http://localhost:8080/trace"
    },
    "autoconfig": {
      "href": "http://localhost:8080/autoconfig"
    },
    "metrics": {
      "href": "http://localhost:8080/metrics"
    },
    "env": {
      "href": "http://localhost:8080/env"
    },
    "health": {
      "href": "http://localhost:8080/health"
    },
    "hystrix.stream": {
      "href": "http://localhost:8080/hystrix.stream"
    },
    "configprops": {
      "href": "http://localhost:8080/configprops"
    },
    "restart": {
      "href": "http://localhost:8080/restart"
    },
    "info": {
      "href": "http://localhost:8080/info"
    },
    "pause": {
      "href": "http://localhost:8080/pause"
    },
    "mappings": {
      "href": "http://localhost:8080/mappings"
    },
    "refresh": {
      "href": "http://localhost:8080/refresh"
    },
    "beans": {
      "href": "http://localhost:8080/beans"
    },
    "resume": {
      "href": "http://localhost:8080/resume"
    },
    "dump": {
      "href": "http://localhost:8080/dump"
    },
    "archaius": {
      "href": "http://localhost:8080/archaius"
    }
  }
}
```

##TODO
- Prepare infrastructure (i.e. Service registry, gateway proxy, etc.) to turn this app into a cloud native microservice.
- Provide better health monitoring and graphs for monitoring infrastructure
