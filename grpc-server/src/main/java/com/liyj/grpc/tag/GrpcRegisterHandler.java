package com.liyj.grpc.tag;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * tag 的解析类
 * @author liyj
 *
 */
public class GrpcRegisterHandler extends NamespaceHandlerSupport{

	public void init() {
		// TODO Auto-generated method stub
		registerBeanDefinitionParser("serverConfig", new ServerConfigDefinitionParser());
	}

}
