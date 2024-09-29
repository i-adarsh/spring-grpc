package com.adarsh.grpcserver.service;

import com.adarsh.grpcserver.proto.HelloWorldRequest;
import com.adarsh.grpcserver.proto.HelloWorldResponse;
import com.adarsh.grpcserver.proto.HelloWorldServiceGrpc;
import io.grpc.stub.StreamObserver;

import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class HelloWorldService extends HelloWorldServiceGrpc.HelloWorldServiceImplBase {

  @Override
  public void hello(HelloWorldRequest request,
      StreamObserver<HelloWorldResponse> responseObserver) {
    String message = request.getMessage();
    System.out.println("gRPC message: " + message);
    HelloWorldResponse helloWorldResponse = HelloWorldResponse.newBuilder()
        .setMessage("Response from gRPC server")
        .build();
    responseObserver.onNext(helloWorldResponse);
    responseObserver.onCompleted();
  }
}
