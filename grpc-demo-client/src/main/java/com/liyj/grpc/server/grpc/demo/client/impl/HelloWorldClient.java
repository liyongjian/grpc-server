package com.liyj.grpc.server.grpc.demo.client.impl;

import java.io.File;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.SSLException;

import com.liyj.grpc.server.grpc.demo.client.interceptor.HeaderClientInterceptor;
import com.liyj.grpc.server.grpc.demo.protocol.helloworld.GreeterGrpc;
import com.liyj.grpc.server.grpc.demo.protocol.helloworld.HelloReply;
import com.liyj.grpc.server.grpc.demo.protocol.helloworld.HelloRequest;

import io.grpc.Channel;
import io.grpc.ClientInterceptors;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;
import io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.NettyChannelBuilder;
import io.grpc.stub.StreamObserver;
/**
 * call interface implement.
 * @author liyj-pc
 *
 */
public class HelloWorldClient {
	  private static final Logger logger = Logger.getLogger(HelloWorldClient.class.getName());
	  private final ManagedChannel channel;
	  //sync
	  private final GreeterGrpc.GreeterBlockingStub blockingStub;
	  //async
	  private final GreeterGrpc.GreeterStub asuncStub;
	  public HelloWorldClient(String host, int port) throws SSLException {
	   //plaintext mode
	   /*channel = ManagedChannelBuilder.forAddress(host, port)
	   		.usePlaintext(true)
	    	.build();*/
		channel = NettyChannelBuilder.forAddress(host, port)
			    .sslContext(GrpcSslContexts.forClient()
			    		.trustManager(new File("E:\\d\\tools\\GRPC\\work\\test\\cert\\ca.cer")).build())
			    .build();
	    Channel tempChannel = ClientInterceptors.intercept(channel, new HeaderClientInterceptor());
	    blockingStub = GreeterGrpc.newBlockingStub(tempChannel);
	    asuncStub = GreeterGrpc.newStub(tempChannel);
	  }
	  
	  /** sync interface. */
	  public void sayHelloworld(String name) {
	    logger.info("Will try to greet " + name + " ...");
	    HelloRequest helloworldRequest = HelloRequest.newBuilder().setName(name).build();
	    HelloReply response;
	    try {
	      response = blockingStub.sayHello(helloworldRequest);
	    } catch (StatusRuntimeException e) {
	      logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
	      return;
	    }
	    logger.info("Greeting: " + response.getMessage());
	  }
	  /**
	   * async interface.
	   * @param name
	   */
	  public void sayHelloworldAsync(String name){
		    logger.info("Will try to greet " + name + " ...");;
		    final ArrayBlockingQueue<HelloReply> responseQ = new ArrayBlockingQueue<>(1);
		    StreamObserver<HelloReply> responseObserver = new StreamObserver<HelloReply>() {

	            @Override
	            public void onNext(HelloReply value) {
	            	responseQ.offer(value);
	            }

	            @Override
	            public void onError(Throwable t) {
	                // TODO Auto-generated method stub

	            }
	            @Override
	            public void onCompleted() {
	                System.out.println("recv response complete!");
	            }
	        };
		    
		    HelloRequest helloworldRequest = HelloRequest.newBuilder().setName(name).build();
		    HelloReply response = null;
		    try {
		    	asuncStub.sayHello(helloworldRequest, responseObserver);
		    	response = responseQ.poll(10L, TimeUnit.SECONDS);
		    } catch (StatusRuntimeException e) {
		      logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
		      return;
		    } catch (InterruptedException ie) {
		    	logger.log(Level.WARNING ,"RPC call timeout: ", ie.getMessage());
		    }
		    logger.info("Greeting: " + response.getMessage());
	  }
	  public void shutdown() throws InterruptedException {
		   channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
	 }
}
