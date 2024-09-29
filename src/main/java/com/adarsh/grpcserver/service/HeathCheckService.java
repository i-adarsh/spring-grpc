package com.adarsh.grpcserver.service;

import com.adarsh.grpcserver.proto.HealthGrpc;
import com.adarsh.grpcserver.proto.HealthOuterClass.HealthCheckRequest;
import com.adarsh.grpcserver.proto.HealthOuterClass.HealthCheckResponse;
import com.adarsh.grpcserver.proto.HealthOuterClass.HealthCheckResponse.ServingStatus;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class HeathCheckService extends HealthGrpc.HealthImplBase {

  @Override
  public void check(HealthCheckRequest request,
      StreamObserver<HealthCheckResponse> responseObserver) {
    System.out.println(request.getService());
    HealthCheckResponse healthCheckResponse = HealthCheckResponse.newBuilder()
        .setStatus(ServingStatus.SERVING)
        .build();
    responseObserver.onNext(healthCheckResponse);
    responseObserver.onCompleted();
  }
}
