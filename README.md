```shell
docker buildx build --platform linux/amd64,linux/arm64 -t   spring-grpc:v1 .
docker run -d -p 9090:9090 --name grpc-container  spring-grpc:v1
```