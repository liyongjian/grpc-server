package com.liyj.grpc.demo.server;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.liyj.grpc.register.GrpcService;

/**
 * test
 * @author liyj-pc
 *
 */
public class GrpcRegisterTest {
    @SuppressWarnings("resource")
	public static void main(String[] argv) {
    	//Start spring container
		ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		//Get service configuration
    	GrpcService service = (GrpcService)ctx.getBean("grpcService");
    	System.out.println(service.getHost() + ":" + service.getPort());
    }
}
