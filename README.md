### Overview of the Project

This project consists of two main components:

* **GRPC-Server:** 
  A gRPC server that listens on port 9090 and supports TLS for secure communication.


* **Spring Cloud Gateway:**
  A Spring Cloud Gateway application that acts as a reverse proxy, forwarding HTTP requests to the gRPC server over HTTP/2 with TLS.

Both applications are configured to use TLS for secure communication.


## GRPC-Server

### Purpose:

The grpc-server is a gRPC-based server that listens on port 9090 and supports secure communication via TLS.
It is configured to use a certificate (certificate.pem) and private key (private.key) for TLS.

### Configuration:

The configuration is defined in application.yaml:
```
grpc:
  enabled: true
  server:
    port: 9090
    security:
      enabled: true
      certificate-chain: classpath:certificate.pem
      private-key: classpath:private.key
    http2:
      enabled: true
```
<!-- true
Port: 9090
TLS: Enabled with the certificate and private key provided in the classpath. -->


### How to Run:

Navigate to the grpc-server directory:

1. server
Start the server using Maven:
```
cd grpc-server
mvn spring-boot:run
```

2. gateway
Purpose:

The gateway is a Spring Cloud Gateway application that acts as a reverse proxy.
It forwards HTTP requests to the grpc-server over HTTP/2 with TLS.
It includes a route (/hello/**) that maps to the gRPC server's sayHello method in the HelloWorldService.
Configuration:

The configuration is defined in application.yaml:
```
server:
  port: 8080
  http2:
    enabled: true
  ssl:
    enabled: true
    key-store: classpath:keystore.p12
    key-store-password: password
    key-store-type: PKCS12

spring:
  cloud:
    gateway:
      httpclient:
        ssl:
          trustedX509Certificates:
            - classpath:certificate.pem
      routes:
        - id: json-hello
          uri: https://localhost:9090
          predicates:
            - Path=/hello/**
          filters:
            - name: JsonToGrpc
              args:
                protoDescriptor: classpath:hello.pb
                protoFile: classpath:hello.proto
                service: HelloWorldService
                method: sayHello
```

<!-- sayHello
Port: 8080
TLS: Enabled with a keystore (keystore.p12) and trusted certificate (certificate.pem).
Route:
Maps /hello/** to the gRPC server (https://localhost:9090).
Uses the JsonToGrpc filter to convert JSON requests to gRPC calls. -->


### How to Run:

Navigate to the gateway directory:
gateway
Start the gateway using Maven:

```
cd gateway
mvn spring-boot:run
```
### Testing the Spring Cloud Gateway with TLS

#### Prerequisites

1. Ensure the following files are present in the classpath:

certificate.pem: The public certificate for TLS.
private.key: The private key for the gRPC server.
keystore.p12: The keystore for the gateway.
hello.pb and hello.proto: The gRPC protocol buffer descriptor and definition files.
Install Java and Maven on your system.

Verify that ports 8080 (gateway) and 9090 (gRPC server) are available.

### Steps to Test
Start the gRPC Server:

Navigate to the grpc-server directory and run:
```
cd grpc-server
mvn spring-boot:run
```
The server will start on https://localhost:9090.
Start the Gateway:

Navigate to the gateway directory and run:
```
cd gateway
mvn spring-boot:run
```
The gateway will start on https://localhost:8080.
Send a Request to the Gateway:

Use a tool like curl or Postman to send a request to the gateway.
Example curl command:
```
curl -k -X POST https://localhost:8080/hello/sayHello \
  -H "Content-Type: application/json" \
  -d '{"name": "John"}'
```
The gateway will forward the request to the gRPC server, and you should receive a response.

Verify the Response:

Check the response from the gateway to ensure it matches the expected output from the gRPC server.


### How TLS Works in This Setup
grpc-server:

The server uses its private key (private.key) and public certificate (certificate.pem) to establish a secure connection with clients.
The certificate is used to prove the server's identity to clients.
gateway:

The gateway uses its keystore (keystore.p12) to secure communication with clients.
It also uses the trusted certificate (certificate.pem) to verify the identity of the grpc-server when forwarding requests.
Client Communication:

Clients (e.g., curl or Postman) communicate with the gateway over HTTPS (https://localhost:8080).
The gateway forwards the request to the grpc-server over HTTPS (https://localhost:9090).

### Summary
The grpc-server is a secure gRPC server that listens on port 9090 and supports HTTP/2 with TLS.
The gateway is a Spring Cloud Gateway application that forwards HTTP requests to the gRPC server over HTTP/2 with TLS.
To test the setup, start both applications and send a request to the gateway, which will forward it to the gRPC server.


## Run grpc-server

```
  cd grpc-server
  mvn spring-boot:run
```

## Run gateway

```
  cd grpc-server
  mvn spring-boot:run
```

### Test  
To test the setup, start both applications and send a request to the gateway, which will forward it to the gRPC server.