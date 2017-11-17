package com.liyj.grpc.server.grpc.demo.server.impl;

import com.liyj.grpc.server.grpc.demo.protocol.helloworld.GreeterGrpc.GreeterImplBase;
import com.liyj.grpc.server.grpc.demo.server.interceptor.HeaderServerInterceptor;

import java.util.logging.Logger;

import com.liyj.grpc.server.grpc.demo.protocol.helloworld.HelloReply;
import com.liyj.grpc.server.grpc.demo.protocol.helloworld.HelloRequest;

import io.grpc.stub.StreamObserver;
/**
 * 接口实现
 * @author liyj-pc
 *
 */
public class HelloWorldImpl extends GreeterImplBase {
	private static final Logger logger = Logger.getLogger(HeaderServerInterceptor.class.getName());
	@Override
	public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
		logger.info("receive name:" + request.getName());
		HelloReply response = HelloReply.newBuilder().setMessage("Hello " + request.getName()).build();
		logger.info("reply message:" + response.getMessage());
	    responseObserver.onNext(response);
	    responseObserver.onCompleted();
	}
}
