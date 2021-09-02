# Read Me First
The following was discovered as part of building this project:

* The original package name 'com.example.spring-boot-docker' is invalid and this project uses 'com.example.springbootdocker' instead.

# Getting Started

To build the image on the server:
```console
mvn package && java -jar target/templateservice-0.0.1-SNAPSHOT.jar
docker build -t springio/gs-spring-boot-docker .
```


To run the image on the server (note external port is 8081 mapped to 8080 internally: 
```console
docker run -p 8081:8080 -t springio/gs-spring-boot-docker
```

To delete un used images
```console
docker rmi $(docker images -qa -f 'dangling=true')
```

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.4.10/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.4.10/maven-plugin/reference/html/#build-image)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.5.4/reference/htmlsingle/#boot-features-developing-web-applications)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)

