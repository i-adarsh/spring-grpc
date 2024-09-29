> #### Docker Hub Link - [https://hub.docker.com/r/iadarshkr/spring-grpc](https://hub.docker.com/r/iadarshkr/spring-grpc)

> ### POM file for starting with gRPC in Spring Boot

```xml
  <properties>
    <java.version>21</java.version>
    <protobuf.version>3.23.4</protobuf.version>
    <protobuf-plugin.version>0.6.1</protobuf-plugin.version>
    <grpc.version>1.58.0</grpc.version>
  </properties>

  <dependencies>
  
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter</artifactId>
    </dependency>

    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-test</artifactId>
      <scope>test</scope>
    </dependency>

    <dependency>
      <groupId>net.devh</groupId>
      <artifactId>grpc-server-spring-boot-starter</artifactId>
      <version>3.1.0.RELEASE</version>
    </dependency>

    <dependency>
      <groupId>io.grpc</groupId>
      <artifactId>grpc-stub</artifactId>
      <version>${grpc.version}</version>
    </dependency>

    <dependency>
      <groupId>io.grpc</groupId>
      <artifactId>grpc-protobuf</artifactId>
      <version>${grpc.version}</version>
    </dependency>

    <dependency>
      <!-- Java 9+ compatibility - Do NOT update to 2.0.0 -->
      <groupId>jakarta.annotation</groupId>
      <artifactId>jakarta.annotation-api</artifactId>
      <version>1.3.5</version>
      <optional>true</optional>
    </dependency>
  
  </dependencies>

  <build>
    <extensions>
      <extension>
        <groupId>kr.motd.maven</groupId>
        <artifactId>os-maven-plugin</artifactId>
        <version>1.7.0</version>
      </extension>
    </extensions>

    <plugins>
      <plugin>
        <groupId>org.xolstice.maven.plugins</groupId>
        <artifactId>protobuf-maven-plugin</artifactId>
        <version>${protobuf-plugin.version}</version>
        <configuration>
          <protocArtifact>com.google.protobuf:protoc:${protobuf.version}:exe:${os.detected.classifier}</protocArtifact>
          <pluginId>grpc-java</pluginId>
          <pluginArtifact>io.grpc:protoc-gen-grpc-java:${grpc.version}:exe:${os.detected.classifier}</pluginArtifact>
        </configuration>
        <executions>
          <execution>
            <goals>
              <goal>compile</goal>
              <goal>compile-custom</goal>
            </goals>
          </execution>
        </executions>
      </plugin>

    </plugins>
  </build>
```

>### Build and push Docker image to Docker Hub and Artifact Registry

#### `Step 1:` Build the docker (container) image

```shell

docker build -t grpc-java:v3 .

docker buildx build --platform linux/amd64,linux/arm64 -t spring-grpc:v1 .
```

#### `Step 2:` Run the docker container and make gRPC curl requests

```shell
docker run -d -p 9090:9090 --name grpc-container  spring-grpc:v1

grpcurl --plaintext -d '{"service": "healthy and serving"}' localhost:9090 Health/Check

grpcurl --plaintext -d '{"message": "Hello "}' localhost:9090 HelloWorldService/hello
```

#### `Step 3:` Tag the image and push it to Docker Hub or Artifact Registry

```shell
docker tag spring-grpc:v1 iadarshkr/spring-grpc:v1

docker push iadarshkr/spring-grpc:v1

docker tag spring-grpc:v1 asia.gcr.io/devops-hq/spring-grpc/spring-grpc:v1

gcloud config set core/project devops-hq

gcloud auth configure-docker asia.gcr.io

docker push asia.gcr.io/devops-hq/spring-grpc/spring-grpc:v1

docker run -d -p 9090:9090 --name grpc-container asia.gcr.io/devops-hq/spring-grpc/spring-grpc:v1
```

> ### Accessing cloud run service through gRPC

#### `Step 1`: Get the endpoint of your Cloud Run Service
```shell
ENDPOINT=$(\
  gcloud run services list \
  --project=${PROJECT} \
  --region=${REGION} \
  --platform=managed \
  --format="value(status.address.url)" \
  --filter="metadata.name=name") 
ENDPOINT=${ENDPOINT#https://} && echo ${ENDPOINT}
```
#### `Step 2`: Make gRPC curl requests to your service 
```shell
grpcurl \
    -proto src/main/proto/hello.proto \
    -d '{"message": "Hello from DevOps HQ!"}' \
    spring-java-w5jk7e3kva-uc.a.run.app:443 \
    HelloWorldService.hello
    
grpcurl \
    -proto src/main/proto/health.proto \
    -d '{"service": "Healthy"}' \
    spring-java-w5jk7e3kva-uc.a.run.app:443 \
    Health.Check
```
> #### Make 5000  calls to your service continuously  
```shell
for i in {1..5000}; do
  grpcurl -proto src/main/proto/health.proto -d '{"service": "healthy and serving"}' spring-grpc-jce3kva-em.a.run.app:443 Health.Check
  grpcurl -proto src/main/proto/hello.proto -d '{"message": "Hello"}' spring-grpc-jce3kva-em.a.run.app:443 HelloWorldService.hello
done
```