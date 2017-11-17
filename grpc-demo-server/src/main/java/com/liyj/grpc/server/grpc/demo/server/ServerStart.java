package com.liyj.grpc.server.grpc.demo.server;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 服务启动.
 * @author liyj-pc
 *
 */
public class ServerStart {
	public static void main( String[] args ) {
    	//Start spring container
		@SuppressWarnings("resource")
		ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
    }
}
