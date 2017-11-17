package com.liyj.grpc.server.grpc.demo.client;

import javax.net.ssl.SSLException;

import com.liyj.grpc.server.grpc.demo.client.impl.HelloWorldClient;

/**
 * Hello world start!
 * @author liyj-pc
 *
 */
public class DemoStart {
	//if HTTPS ,The host is the CN or sub-domain of the certificate.
	public static String host = "127.0.0.1";
	public static int port = 50052;
    public static void main( String[] args ) throws SSLException,
    		InterruptedException {
	    HelloWorldClient clientX = new HelloWorldClient(host, 50052);
	    try {
	      String user = "world!";
	      if (args.length > 0) {
	        user = args[0];
	      }
	      clientX.sayHelloworldAsync(user);
	      System.out.println("Call is ok.");
	    } finally {
	      clientX.shutdown();
	    }
    }
}
