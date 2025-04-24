package com.grpc_server.demo.service;

import com.example.service.grpc.HelloRequest;
import com.example.service.grpc.HelloResponse;
import com.example.service.grpc.HelloWorldServiceGrpc;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
class GrettingService extends HelloWorldServiceGrpc.HelloWorldServiceImplBase {

  @Override
  public void sayHello(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
    HelloResponse response = HelloResponse.newBuilder().setMessage("Hey " +
        request.getName()).build();
    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

}
